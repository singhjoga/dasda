import { MatSort, MatTableDataSource } from '@angular/material';

export class CustomMatTableDataSource<T> extends MatTableDataSource<T> {

  sortData: ((data: T[], sort: MatSort) => T[]) = (data: T[], sort: MatSort): T[] => {
    const active = sort.active;
    const direction = sort.direction;
    if (!active || direction === '') { return data; }
    return data.sort((a, b) => {
      const valueA = this.sortingDataAccessor(a, active) ? this.sortingDataAccessor(a, active) : a[active];
      const valueB = this.sortingDataAccessor(b, active) ? this.sortingDataAccessor(b, active) : b[active];
      const valueAIsNumber = !isNaN(Number(a));
      const valueBIsNumber = !isNaN(Number(b));

      let comparatorResult = null;
      if (valueAIsNumber && valueBIsNumber) {
        comparatorResult = Number(valueA) > Number(valueB) ? 1 : -1;
      } else {
        const _compareFn = new Intl.Collator('en', { sensitivity: 'base', numeric: false }).compare;
        comparatorResult = _compareFn(valueA as string, valueB as string);
      }

      return comparatorResult * (direction === 'asc' ? 1 : -1);
    });
  }
}