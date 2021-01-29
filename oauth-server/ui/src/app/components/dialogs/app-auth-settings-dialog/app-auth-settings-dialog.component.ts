import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { FormBuilder, Validators, FormGroup } from '@angular/forms';
import { DynamicDialogRef, DynamicDialogConfig, ConfirmationService, SelectItem, MessageService } from 'primeng/api';
import { AppAuthSetting } from 'src/app/models/app-auth-setting.model';
import {
  PROVIDER_TYPES,
  PROVIDER_ACTIVE_DIRECTORY,
  PROVIDER_EXERNAL_DATABASE,
  PROVIDER_INTERNAL_DATABASE,
  PROVIDER_LDAP,
  PASSWORD_ENCODER
} from 'src/app/shared/consts';
import { AppAuthSettingService } from 'src/app/services/app-auth-setting.service';
import { tap } from 'rxjs/operators';

@Component({
  selector: 'app-app-auth-settings-dialog',
  templateUrl: './app-auth-settings-dialog.component.html',
  styleUrls: ['./app-auth-settings-dialog.component.sass']
})
export class AppAuthSettingsDialogComponent implements OnInit {

  private setting: AppAuthSetting;
  public settingsJSON;
  private mode = '';
  public providerForm: FormGroup;
  public PROVIDER_ACTIVE_DIRECTORY = PROVIDER_ACTIVE_DIRECTORY;
  public PROVIDER_EXERNAL_DATABASE = PROVIDER_EXERNAL_DATABASE;
  public PROVIDER_INTERNAL_DATABASE = PROVIDER_INTERNAL_DATABASE;
  public PROVIDER_LDAP = PROVIDER_LDAP;

  constructor(
    private formBuilder: FormBuilder,
    public ref: DynamicDialogRef,
    public config: DynamicDialogConfig,
    private cdRef: ChangeDetectorRef,
    private confirmationService: ConfirmationService,
    private appAuthSettingService: AppAuthSettingService,
    private messageService: MessageService
  ) {
    this.mode = config.data.mode;
    this.setting = config.data.setting;
    this.settingsJSON = this.setting ? JSON.parse(this.setting.settingsJson) : '';
  }

  providerTypes: SelectItem[] = [];
  passwordEncoders: SelectItem[] = [];

  // Form Auth Settings
  readonly FORM_DISPLAY_ORDER = 'displayOrder';
  readonly FORM_NAME = 'name';
  readonly FORM_DESCRIPTION = 'description';
  readonly FORM_PROVIDER_TYPE = 'providerType';
  readonly FORM_CAN_AUTHENTICATE = 'canAuthenticate';
  readonly FORM_CAN_AUTHORIZE = 'canAuthorize';
  readonly FORM_USER_NOT_FOUND = 'whenUserNotFound';
  readonly FORM_ROLES_NOT_FOUND = 'whenRolesNotFound';
  readonly FORM_ROLES_FOUND = 'whenRolesFound';

  // Form Internal Database Options
  readonly FORM_AUTHORITIES_TYPE = 'returnAuthorityType';

  // Form External Database Options
  readonly FORM_JBDC_URL = 'jdbcUrl';
  readonly FORM_DRIVER_CLASS = 'driverClass';
  readonly FORM_USERNAME = 'username';
  readonly FORM_PASSWORD = 'password';
  readonly FORM_AUTHENTICATE_SQL = 'authenticateSQL';
  readonly FORM_AUTHORIZE_SQL = 'authorizeSQL';
  readonly FORM_PASSWORD_ENCODER_SETTING = 'passwordEncoderSetting';

  // For Active Directory
  readonly FORM_LDAP_URL = 'ldapUrl'; // Also used for LDAP
  readonly FORM_DOMAIN_NAME = 'domainName';
  readonly FORM_MANAGER_DN = 'managerDn'; // Also used for LDAP
  readonly FORM_MANAGER_PASSWORD = 'managerPassword'; // Also used for LDAP
  readonly FORM_SEARCH_BASE = 'searchBase';
  readonly FORM_SEARCH_FILTER = 'searchFilter';

  // For LDAP
  readonly FORM_USER_DN_PATTERN = 'userDnPattern';
  readonly FORM_USER_SEARCH_BASE = 'userSearchBase';
  readonly FORM_USER_SEARCH_FILTER = 'userSearchFilter';
  readonly FORM_GROUP_SEARCH_BASE = 'groupSearchBase';
  readonly FORM_GROUP_SEARCH_FILTER = 'groupSearchFilter';
  readonly FORM_GROUP_ROLE_ATTRIBUTE = 'groupRoleAttribute';

  ngOnInit() {
    PROVIDER_TYPES.forEach(element => {
      this.providerTypes.push({ label: element, value: element });
    });

    PASSWORD_ENCODER.forEach(element => {
      this.passwordEncoders.push({ label: element, value: element });
    });

    this.providerForm = this.formBuilder.group({
      // Form Auth Settings
      [this.FORM_DISPLAY_ORDER]: [this.setting ? this.setting.displayOrder : '', [Validators.required]],
      [this.FORM_NAME]: [this.setting ? this.setting.name : '', [Validators.required]],
      [this.FORM_DESCRIPTION]: [this.setting ? this.setting.description : '', [Validators.required]],
      [this.FORM_PROVIDER_TYPE]: [this.setting ? this.setting.providerType : '', [Validators.required]],
      [this.FORM_CAN_AUTHENTICATE]: [this.setting ? this.settingsJSON.canAuthenticate : false, [Validators.required]],
      [this.FORM_CAN_AUTHORIZE]: [this.setting ? this.settingsJSON.canAuthorize : false, [Validators.required]],
      [this.FORM_USER_NOT_FOUND]: [this.setting ? this.settingsJSON.whenUserNotFound : false, [Validators.required]],
      [this.FORM_ROLES_NOT_FOUND]: [this.setting ? this.settingsJSON.whenRolesNotFound : false, [Validators.required]],
      [this.FORM_ROLES_FOUND]: [this.setting ? this.settingsJSON.whenRolesFound : false, [Validators.required]],

      // Form Internal Database Options
      [this.FORM_AUTHORITIES_TYPE]: [this.setting ? this.settingsJSON.returnAuthorityType : false/* , [Validators.required] */],

      // Form External Database Options
      [this.FORM_JBDC_URL]: [this.setting ? this.settingsJSON.jdbcUrl : ''/* , [Validators.required] */],
      [this.FORM_DRIVER_CLASS]: [this.setting ? this.settingsJSON.driverClass : ''/* , [Validators.required] */],
      [this.FORM_USERNAME]: [this.setting ? this.settingsJSON.username : ''/* , [Validators.required] */],
      [this.FORM_PASSWORD]: [this.setting ? this.settingsJSON.password : ''/* , [Validators.required] */],
      [this.FORM_AUTHENTICATE_SQL]: [this.setting ? this.settingsJSON.authenticateSQL : ''/* , [Validators.required] */],
      [this.FORM_AUTHORIZE_SQL]: [this.setting ? this.settingsJSON.authorizeSQL : ''/* , [Validators.required] */],
      [this.FORM_PASSWORD_ENCODER_SETTING]: [this.setting ? this.settingsJSON.passwordEncoderSetting : ''/* , [Validators.required] */],

      // For Active Directory
      [this.FORM_LDAP_URL]: [this.setting ? this.settingsJSON.jdbcUrl : ''/* , [Validators.required] */],
      [this.FORM_DOMAIN_NAME]: [this.setting ? this.settingsJSON.driverClass : ''/* , [Validators.required] */],
      [this.FORM_SEARCH_BASE]: [this.setting ? this.settingsJSON.username : ''/* , [Validators.required] */],
      [this.FORM_SEARCH_FILTER]: [this.setting ? this.settingsJSON.password : ''/* , [Validators.required] */],
      [this.FORM_MANAGER_DN]: [this.setting ? this.settingsJSON.authenticateSQL : ''/* , [Validators.required] */],
      [this.FORM_MANAGER_PASSWORD]: [this.setting ? this.settingsJSON.authorizeSQL : ''/* , [Validators.required] */],

      // For LDAP
      [this.FORM_USER_DN_PATTERN]: [this.setting ? this.settingsJSON.userDnPattern : ''/* , [Validators.required] */],
      [this.FORM_USER_SEARCH_BASE]: [this.setting ? this.settingsJSON.userSearchBase : ''/* , [Validators.required] */],
      [this.FORM_USER_SEARCH_FILTER]: [this.setting ? this.settingsJSON.userSearchFilter : ''/* , [Validators.required] */],
      [this.FORM_GROUP_SEARCH_BASE]: [this.setting ? this.settingsJSON.groupSearchBase : ''/* , [Validators.required] */],
      [this.FORM_GROUP_SEARCH_FILTER]: [this.setting ? this.settingsJSON.groupSearchFilter : ''/* , [Validators.required] */],
      [this.FORM_GROUP_ROLE_ATTRIBUTE]: [this.setting ? this.settingsJSON.groupRoleAttribute : ''/* , [Validators.required] */],
      [this.FORM_PASSWORD_ENCODER_SETTING]: [this.setting ? this.settingsJSON.passwordEncoderSetting : ''/* , [Validators.required] */]

    });
  }

  onProviderTypeChanged() {
    if (this.providerForm.controls[this.FORM_PROVIDER_TYPE].value === this.PROVIDER_INTERNAL_DATABASE) {
      this.providerForm.controls[this.FORM_AUTHORITIES_TYPE].setValidators([Validators.required]);
      this.providerForm.controls[this.FORM_AUTHORITIES_TYPE].updateValueAndValidity();
    }
  }

  onPasswordEncoderChanged() {

  }

  getProviderType() {
    return this.providerForm.controls[this.FORM_PROVIDER_TYPE].value;
  }

  canAuthenticate() {
    console.log(this.providerForm.controls[this.FORM_CAN_AUTHENTICATE].value);
    return this.providerForm.controls[this.FORM_CAN_AUTHENTICATE].value;
  }

  canAuthorize() {
    console.log(this.providerForm.controls[this.FORM_CAN_AUTHORIZE].value);
    return this.providerForm.controls[this.FORM_CAN_AUTHORIZE].value;
  }

  save() {
    const selectedProvider = this.providerForm.controls[this.FORM_PROVIDER_TYPE].value;
    let settingsJson = '';

    if (selectedProvider === this.PROVIDER_ACTIVE_DIRECTORY) {
      settingsJson = JSON.stringify({
        whenUserNotFound: this.providerForm.controls[this.FORM_USER_NOT_FOUND].value,
        whenRolesNotFound: this.providerForm.controls[this.FORM_ROLES_NOT_FOUND].value,
        whenRolesFound: this.providerForm.controls[this.FORM_ROLES_FOUND].value,
        ldapUrl: this.providerForm.controls[this.FORM_LDAP_URL].value,
        domainName: this.providerForm.controls[this.FORM_DOMAIN_NAME].value,
        managerDn: this.providerForm.controls[this.FORM_MANAGER_DN].value,
        managerPassword: this.providerForm.controls[this.FORM_MANAGER_PASSWORD].value,
        searchBase: this.providerForm.controls[this.FORM_SEARCH_BASE].value,
        searchFilter: this.providerForm.controls[this.FORM_SEARCH_FILTER].value,
        canAuthenticate: this.providerForm.controls[this.FORM_CAN_AUTHENTICATE].value,
        canAuthorize: this.providerForm.controls[this.FORM_CAN_AUTHORIZE].value,
      });
    } else if (selectedProvider === this.PROVIDER_EXERNAL_DATABASE) {
      settingsJson = JSON.stringify({
        whenUserNotFound: this.providerForm.controls[this.FORM_USER_NOT_FOUND].value,
        whenRolesNotFound: this.providerForm.controls[this.FORM_ROLES_NOT_FOUND].value,
        whenRolesFound: this.providerForm.controls[this.FORM_ROLES_FOUND].value,
        jdbcUrl: this.providerForm.controls[this.FORM_JBDC_URL].value,
        driverClass: this.providerForm.controls[this.FORM_DRIVER_CLASS].value,
        username: this.providerForm.controls[this.FORM_USERNAME].value,
        password: this.providerForm.controls[this.FORM_PASSWORD].value,
        authenticateSQL: this.providerForm.controls[this.FORM_AUTHENTICATE_SQL].value,
        authorizeSQL: this.providerForm.controls[this.FORM_AUTHENTICATE_SQL].value,
        passwordEncoderSetting: this.providerForm.controls[this.FORM_PASSWORD_ENCODER_SETTING].value,
        canAuthenticate: this.providerForm.controls[this.FORM_CAN_AUTHENTICATE].value,
        canAuthorize: this.providerForm.controls[this.FORM_CAN_AUTHORIZE].value,
      });
    } else if (selectedProvider === this.PROVIDER_INTERNAL_DATABASE) {
      settingsJson = JSON.stringify({
        whenUserNotFound: this.providerForm.controls[this.FORM_USER_NOT_FOUND].value,
        whenRolesNotFound: this.providerForm.controls[this.FORM_ROLES_NOT_FOUND].value,
        whenRolesFound: this.providerForm.controls[this.FORM_ROLES_FOUND].value,
        returnAuthorityType: this.providerForm.controls[this.FORM_AUTHORITIES_TYPE].value,
        canAuthenticate: this.providerForm.controls[this.FORM_CAN_AUTHENTICATE].value,
        canAuthorize: this.providerForm.controls[this.FORM_CAN_AUTHORIZE].value,
      });
    } else if (selectedProvider === this.PROVIDER_LDAP) {
      settingsJson = JSON.stringify({
        ldapUrl: this.providerForm.controls[this.FORM_USER_NOT_FOUND].value,
        userDnPattern: this.providerForm.controls[this.FORM_ROLES_NOT_FOUND].value,
        userSearchBase: this.providerForm.controls[this.FORM_ROLES_FOUND].value,
        userSearchFilter: this.providerForm.controls[this.FORM_JBDC_URL].value,
        groupSearchBase: this.providerForm.controls[this.FORM_DRIVER_CLASS].value,
        groupSearchFilter: this.providerForm.controls[this.FORM_USERNAME].value,
        groupRoleAttribute: this.providerForm.controls[this.FORM_PASSWORD].value,
        managerDn: this.providerForm.controls[this.FORM_AUTHENTICATE_SQL].value,
        managerPassword: this.providerForm.controls[this.FORM_AUTHENTICATE_SQL].value,
        canAuthenticate: this.providerForm.controls[this.FORM_CAN_AUTHENTICATE].value,
        canAuthorize: this.providerForm.controls[this.FORM_CAN_AUTHORIZE].value,
      });
    }


    if (this.mode === 'create') {
      this.appAuthSettingService.create({
        clientId: '',
        description: this.providerForm.controls[this.FORM_DESCRIPTION].value,
        displayOrder: this.providerForm.controls[this.FORM_DISPLAY_ORDER].value,
        name: this.providerForm.controls[this.FORM_NAME].value,
        providerType: this.providerForm.controls[this.FORM_PROVIDER_TYPE].value,
        settingsJson
      } as AppAuthSetting)
        .pipe(
          (tap((ii) => {
            this.messageService.add({ severity: 'success', summary: 'Success Message', detail: 'Setting added successfully' });
            this.ref.close(ii);
          }))
        )
        .toPromise();
    } else {
      this.appAuthSettingService.update({
        id: this.config.data.setting,
        clientId: '',
        description: this.providerForm.controls[this.FORM_DESCRIPTION].value,
        displayOrder: this.providerForm.controls[this.FORM_DISPLAY_ORDER].value,
        name: this.providerForm.controls[this.FORM_NAME].value,
        providerType: this.providerForm.controls[this.FORM_PROVIDER_TYPE].value,
        settingsJson
      } as AppAuthSetting)
        .pipe(
          (tap((ii) => {
            this.messageService.add({ severity: 'success', summary: 'Success Message', detail: 'Setting updated successfully' });
            this.ref.close(ii);
          }))
        )
        .toPromise();
    }
  }
}
