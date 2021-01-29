package com.harbois.komrade.runner.execution;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.sshd.common.util.io.IoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CopyArchiveFromContainerCmd;
import com.github.dockerjava.api.command.CopyArchiveToContainerCmd;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.CreateNetworkCmd;
import com.github.dockerjava.api.command.ExecCreateCmd;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.api.command.PullImageCmd;
import com.github.dockerjava.api.command.RemoveNetworkCmd;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.PullResponseItem;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import com.github.dockerjava.core.command.PullImageResultCallback;
import com.harbois.komrade.runner.exception.StepFailureException;
import com.harbois.komrade.runner.pipeline.ContainerDef;
import com.harbois.komrade.runner.pipeline.Pipeline;
import com.harbois.komrade.runner.pipeline.Step;

public class DockerExecutor extends AbstractExecutor {
	private static Logger LOG = LoggerFactory.getLogger(DockerExecutor.class);
	private static String DOCKER_HOST = "tcp://localhost:2375";
	private DockerClient docker;
	private String containerId;
	private List<String> linkedContainerIds=new ArrayList<>();
	private String networkName=null;
	public DockerExecutor(Pipeline pipeline, Step step) {
		super(pipeline, step);
		this.networkName="komrade-"+getContext().getJob().getId();
	}

	@Override
	protected void connect(Step step) throws StepFailureException {
		DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder().withDockerHost(DOCKER_HOST).build();
		docker = DockerClientBuilder.getInstance(config).build();
		ContainerDef containerDef = step.getContainer();
		createNetwork();
		containerId=createAndStartContainer(containerDef,false);
	}

	@Override
	protected void disconnect() throws StepFailureException {
		if (containerId==null) {
			return;
		}
		removeContainerWithId(containerId);
		for (String linkedId: linkedContainerIds) {
			removeContainerWithId(linkedId);
		}
		removeNetwork();
	}

	@Override
	protected void upload(File localFile, String remotePath) throws StepFailureException {
		CopyArchiveToContainerCmd cmd = docker.copyArchiveToContainerCmd(containerId);
		cmd.withHostResource(localFile.getAbsolutePath())
		   .withRemotePath(remotePath);
		cmd.exec();
		cmd.close();
	}

	@Override
	protected void download(String remotePath, File localFile) throws StepFailureException {
		CopyArchiveFromContainerCmd cmd = docker.copyArchiveFromContainerCmd(containerId, remotePath);
		cmd.withHostPath(localFile.getAbsolutePath());
		localFile.getParentFile().mkdirs();
		try (TarArchiveInputStream tarStream = new TarArchiveInputStream(cmd.exec());
	
				OutputStream os = new FileOutputStream(localFile)){
			tarStream.getNextTarEntry();
			IoUtils.copy(tarStream, os);
			cmd.close();
		} catch (IOException e) {
			throw new StepFailureException(e);
		}
	}

	@Override
	protected void exec(String cmd) throws StepFailureException {
		exec(cmd,System.out);
	}
	@Override
	protected void exec(String cmd, OutputStream out) throws StepFailureException {
		ExecCreateCmd createCmd=docker.execCreateCmd(containerId);
		if (getWorkingDir() != null) {
			createCmd.withWorkingDir(getWorkingDir());
		}
		//Command is not given as a full string but as tokens
		if (cmd.contains("*")) {
			createCmd.withCmd("sh", "-c",cmd);
		}else {
			createCmd.withCmd(StringUtils.split(cmd));
		}
		LOG.info(cmd);
		ExecCreateCmdResponse createResp=  //
				createCmd.withAttachStderr(true) //
			.withAttachStdout(true)
			.exec();
		LastLineOutputStream os = new LastLineOutputStream(out);
		ExecStartResultCallback resultCallback = new ExecStartResultCallback(os,System.err) {
			@Override
			public void onNext(Frame frame) {
				super.onNext(frame);
			}

			@Override
			public void onError(Throwable e) {
				super.onError(new StepFailureException(e.getMessage()));
			}
			
		};
		docker.execStartCmd(createResp.getId()).exec(resultCallback);
		try {
			resultCallback.awaitCompletion().close();
			//inspect the exit code
			Long exitCode = docker.inspectExecCmd(createResp.getId()).exec().getExitCodeLong();
			if (exitCode != 0) {
				throw new StepFailureException("Step Failed: "+os.getLastLine()+" Exit Code:"+exitCode);
			}
		} catch (InterruptedException | IOException e) {
			throw new StepFailureException(e.getMessage(), e);
		}

	}

	private void createNetwork() {
		CreateNetworkCmd cmd = docker.createNetworkCmd();
		cmd.withName(networkName);
		cmd.withCheckDuplicate(true);
		cmd.exec();
	}
	private void removeNetwork() {
		RemoveNetworkCmd cmd = docker.removeNetworkCmd(networkName);
		cmd.exec();
	}
	private String createAndStartContainer(ContainerDef containerDef, boolean cmdAsBash) throws StepFailureException{
		LOG.info(String.format("Creating docker container '%s'...",containerDef.getAlias()));
		String newContainerId = createContainer(containerDef,cmdAsBash);
		LOG.info(String.format("Starting docker container '%s'...",containerDef.getAlias()));
		docker.startContainerCmd(newContainerId).exec();
		LOG.info(String.format("Docker container started '%s'",containerDef.getAlias()));
		return newContainerId;
	}
	@SuppressWarnings("deprecation")
	private String createContainer(ContainerDef containerDef, boolean cmdAsBash) throws StepFailureException{
		ensureImage(containerDef.getImage());

		CreateContainerCmd cmd = docker.createContainerCmd(containerDef.getImage())
				.withImage(containerDef.getImage());
		cmd.withTty(true);

		if (cmdAsBash) {
			//.withCmd("/bin/bash");
		}
		if (containerDef.getPrivileged()) {
			cmd.withPrivileged(true);
		}
		if (StringUtils.isEmpty(containerDef.getAlias())) {
			throw new StepFailureException(String.format("Container for image '%s' has no alias set", containerDef.getImage()));
		}
		String name=containerDef.getAlias()+"-"+getContext().getJob().getId();

		cmd.withName(name);
		cmd.withNetworkMode(networkName);
		cmd.withAliases(containerDef.getAlias());
		if (!containerDef.getVolumes().isEmpty()) {
			List<Bind> binds = new ArrayList<Bind>();
			for (String volumeDef: containerDef.getVolumes()) {
				String[] parts = StringUtils.split(volumeDef,":");
				if (parts.length < 2 || parts.length > 3) {
					throw new StepFailureException("Not a valid volume defintion: "+volumeDef+". Parts Count: "+parts.length);
				}
				//TODO: AccessMode mapping
				Bind bind = new Bind(parts[0], new Volume(parts[1]));
				binds.add(bind);
			}
			cmd.withBinds(binds);
		}
		if (!containerDef.getDependsOn().isEmpty()) {
			for (String linkAlias: containerDef.getDependsOn()) {
				//find the container def in pipeline linked container
				ContainerDef linkedDef = findLinkedContainer(linkAlias);
				if (linkedDef == null) {
					throw new StepFailureException(String.format("Container with alias '%s' not found in linkedContainers", linkAlias));
				}
				String id = createAndStartContainer(linkedDef,true);
				linkedContainerIds.add(id);
			}
		}
		if (!containerDef.getVariables().isEmpty()) {
			List<String> envs = new ArrayList<>();
			for (String env: containerDef.getVariables().keySet()) {
				String value=containerDef.getVariables().get(env);
				envs.add(env+"="+value);
			}
			cmd.withEnv(envs);
		}
		CreateContainerResponse resp = cmd.exec();
		return resp.getId();
	}

	public void removeContainerWithId(String containerId) throws StepFailureException{
		List<Container> containers = docker.listContainersCmd().withIdFilter(Arrays.asList(containerId)).withShowAll(true).exec();

		if (containers.isEmpty()) {
			LOG.warn(String.format("No containers found with id '%s'.", containerId));
			return;
		}
		removeContainers(containers);
	}
	public void removeContainerWithNames(String... names) throws StepFailureException{
		List<Container> containers = docker.listContainersCmd().withNameFilter(Arrays.asList(names)).withShowAll(true).exec();

		if (containers.isEmpty()) {
			LOG.warn(String.format("No containers found with name(s) '%s'.", names.toString()));
			return;
		}
		removeContainers(containers);
	}

	private void removeContainers(List<Container> containers) {
		for (int i = 0; i < containers.size(); i++) {
			String id = containers.get(i).getId();
			docker.stopContainerCmd(id).exec();
			docker.removeContainerCmd(id).exec();
			LOG.info(String.format("Container '%s' removed", containers.get(i).getNames()[0]));
		}		
	}
	private void ensureImage(String imageName) throws StepFailureException {
		List<Image> images = docker.listImagesCmd().withImageNameFilter(imageName).exec();
		if (!images.isEmpty()) {
			LOG.info(String.format("Image exists '%s'", imageName));
			return;
		}
		String tag=null;
		String registry=null;
		String repo=imageName;
		if (imageName.contains(":")) {
			repo = StringUtils.substringBefore(imageName, ":");
			tag = StringUtils.substringAfter(imageName, ":");
		}
		if (repo.contains("/")) {
			registry = StringUtils.substringBefore(repo, "/");
			repo = StringUtils.substringAfter(repo, "/");
		}
		
		LOG.info(String.format("Pulling image '%s' ...", imageName));
		try {
			PullImageResultCallback resultCallback = new PullImageResultCallback() {
				@Override
				public void onNext(PullResponseItem item) {
					super.onNext(item);
					LOG.info(item.toString());
				}

				@Override
				public void onError(Throwable t) {
					LOG.error(t.getMessage(),t);
					super.onError(new StepFailureException(String.format("Failed to pull image '%s'", imageName)));
				}
				
			};
			//resultCallback.onError(new StepFailureException(String.format("Failed to pull image '%s'", name)));
			PullImageCmd pullImageCmd = docker.pullImageCmd(repo);
			if (registry != null) {
				pullImageCmd.withRegistry(registry);
			}
			if (tag != null) {
				pullImageCmd.withTag(tag);
			}
			pullImageCmd.exec(resultCallback);
			resultCallback.awaitCompletion().close();
		} catch (InterruptedException | IOException e) {
			throw new StepFailureException(e.getMessage(), e);
		}
		LOG.info(String.format("Finished pulling image '%s'", imageName));
	}
	
	@Override
	protected void changeToWorkingDir(String workingDir) throws StepFailureException {
		//do nothing, with every command, working dir is set
	}
	private ContainerDef findLinkedContainer(String alias) {
		for (ContainerDef linked: getPipeline().getContainers()) {
			if (alias.equals(linked.getAlias())) {
				return linked;
			}
		}
		
		return null;
	}

}
