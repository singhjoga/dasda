import { TestBed } from '@angular/core/testing';

import { AppAuthSettingService } from './app-auth-setting.service';

describe('AppAuthSettingService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: AppAuthSettingService = TestBed.get(AppAuthSettingService);
    expect(service).toBeTruthy();
  });
});
