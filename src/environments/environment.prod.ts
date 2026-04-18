import packageInfo from '../../package.json';

/*export const environment = {
  appVersion: packageInfo.version,
  production: true
};*//*local*/
export const environment = {
  production: true,
  apiUrl: 'https://confienvios-app.onrender.com/api',
  authUrl: 'https://confienvios-app.onrender.com/auth',
  appVersion: '1.0.0'
};