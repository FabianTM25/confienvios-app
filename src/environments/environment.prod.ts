import packageInfo from '../../package.json';

/*export const environment = {
  appVersion: packageInfo.version,
  production: true
};*//*local*/
export const environment = {
  production: true,
  apiUrl: 'https://confienvios-app.onrender.com/api',
  appVersion: packageInfo.version
};
