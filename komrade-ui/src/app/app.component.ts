import { Component } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { TranslateService } from '@ngx-translate/core';
import { DEFAULT_LANGUAGE } from './constants';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent {
  title = 'komrade';

  private locale = null;

  readonly USER_AGENT = !window || !window.navigator ? '' : window.navigator.userAgent || '';
  readonly USER_AGENT_CHROME = this.USER_AGENT.indexOf('Chrome') >= 0;
  readonly USER_AGENT_IE = this.USER_AGENT.indexOf('MSIE') >= 0 || this.USER_AGENT.match(/Trident.*rv\:11\./);

  readonly isForcingStart$ = new BehaviorSubject(false);
  readonly isBrowserChrome = this.USER_AGENT_CHROME;
  readonly isBrowserIE = this.USER_AGENT_IE;

  constructor(
    private translate: TranslateService
  ) {
    const locale = localStorage.getItem('locale');
    this.locale = locale ? locale : DEFAULT_LANGUAGE;
    translate.setDefaultLang(this.locale);
    localStorage.setItem('locale', this.locale);
  }
}
