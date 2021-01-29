import { TestBed } from '@angular/core/testing';

import { SubResourceService } from './sub-resource.service';

describe('SubResourceService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: SubResourceService = TestBed.get(SubResourceService);
    expect(service).toBeTruthy();
  });
});
