import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ConfirmationService } from 'primeng/api';
import { AutoCompleteModule } from 'primeng/autocomplete';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { ChartModule } from 'primeng/chart';
import { CheckboxModule } from 'primeng/checkbox';
import { MessageService } from 'primeng/components/common/messageservice';
import { DynamicDialogModule } from 'primeng/components/dynamicdialog/dynamicdialog';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { DialogModule } from 'primeng/dialog';
import { DropdownModule } from 'primeng/dropdown';
import { InputTextModule } from 'primeng/inputtext';
import { MenuModule } from 'primeng/menu';
import { MenubarModule } from 'primeng/menubar';
import { MessageModule } from 'primeng/message';
import { MessagesModule } from 'primeng/messages';
import { PasswordModule } from 'primeng/password';
import { ProgressBarModule } from 'primeng/progressbar';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { TableModule } from 'primeng/table';
import { TabViewModule } from 'primeng/tabview';
import { TieredMenuModule } from 'primeng/tieredmenu';
import { ToastModule } from 'primeng/toast';
import { TooltipModule } from 'primeng/tooltip';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AppAuthSettingsComponent } from './components/app-auth-settings/app-auth-settings.component';
import { ClientsComponent } from './components/clients/clients.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { AppAuthSettingsDialogComponent } from './components/dialogs/app-auth-settings-dialog/app-auth-settings-dialog.component';
import { PasswordDialogComponent } from './components/dialogs/password-dialog/password-dialog.component';
import { SelectGroupsDialogComponent } from './components/dialogs/select-groups-dialog/select-groups-dialog.component';
import { SelectRolesDialogComponent } from './components/dialogs/select-roles-dialog/select-roles-dialog.component';
import { SelectUsersDialogComponent } from './components/dialogs/select-users-dialog/select-users-dialog.component';
import { FooterComponent } from './components/footer/footer.component';
import { GroupsComponent } from './components/groups/groups.component';
import { HeaderComponent } from './components/header/header.component';
import { HomeComponent } from './components/home/home.component';
import { LayoutComponent } from './components/layout/layout.component';
import { LoginComponent } from './components/login/login.component';
import { RolesComponent } from './components/roles/roles.component';
import { UsersComponent } from './components/users/users.component';
import { AuthGuard } from './services/auth-guard.service';
import { Interceptor } from './services/interceptor.service';
import { RadioButtonModule } from 'primeng/radiobutton';






@NgModule({
  declarations: [
    AppComponent,
    LayoutComponent,
    HeaderComponent,
    FooterComponent,
    LoginComponent,
    HomeComponent,
    ClientsComponent,
    DashboardComponent,
    RolesComponent,
    GroupsComponent,
    UsersComponent,
    SelectRolesDialogComponent,
    SelectUsersDialogComponent,
    SelectGroupsDialogComponent,
    PasswordDialogComponent,
    AppAuthSettingsComponent,
    AppAuthSettingsDialogComponent,
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    AppRoutingModule,
    MenuModule,
    MenubarModule,
    HttpClientModule,
    ButtonModule,
    InputTextModule,
    PasswordModule,
    CardModule,
    ToastModule,
    ProgressSpinnerModule,
    ProgressBarModule,
    TableModule,
    TooltipModule,
    DialogModule,
    ConfirmDialogModule,
    MessagesModule,
    MessageModule,
    TieredMenuModule,
    ChartModule,
    CheckboxModule,
    AutoCompleteModule,
    TabViewModule,
    DynamicDialogModule,
    DropdownModule,
    RadioButtonModule,
  ],
  providers: [
    AuthGuard,
    MessageService,
    { provide: HTTP_INTERCEPTORS, useClass: Interceptor, multi: true },
    ConfirmationService
  ],
  bootstrap: [AppComponent],
  schemas: [
    CUSTOM_ELEMENTS_SCHEMA
  ],
  entryComponents: [
    SelectRolesDialogComponent,
    SelectUsersDialogComponent,
    SelectGroupsDialogComponent,
    PasswordDialogComponent,
    AppAuthSettingsDialogComponent
  ]
})
export class AppModule { }
