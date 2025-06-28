declare const VERSION: string;
declare const SERVER_API_URL: string;
declare const DEVELOPMENT: string;
declare const I18N_HASH: string;
declare const ENVIRONMENT: string;
declare const CLOUD_DEPLOYMENT: boolean;
declare const DEBUG: boolean;

declare module '*.json' {
  const value: any;
  export default value;
}
