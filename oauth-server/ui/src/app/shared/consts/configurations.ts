const getUrl = window.location;

export const SERVER_URL = (getUrl.host === 'localhost:4200' ?
    'http://localhost:8080/' : getUrl.protocol + '//' + getUrl.host + ':' + getUrl.port + '/');

export const DIALOIG_WIDTH = '800px';
export const DIALOIG_HEIGHT = '600px';
export const TABLE_ITEMS_PER_PAGE = 10;
export const DIALOG_TABLE_ITEMS_PER_PAGE = 5;
export const TOAST_LIFE = 60000;
