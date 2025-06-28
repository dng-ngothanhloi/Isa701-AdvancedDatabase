module.exports = {
  // APP_VERSION is passed as an environment variable from the Gradle / Maven build tasks.
  VERSION: process.env.APP_VERSION || 'DEV',
  // The root URL for API calls, ending with a '/' - for example: `"https://www.jhipster.tech:8081/myservice/"`.
  // If this URL is left empty (""), then it will be relative to the current context.
  // If you use an API server, in `prod` mode, you will need to enable CORS
  // (see the `jhipster.cors` common JHipster property in the `application-*.yml` configurations)
  // SERVER_API_URL configuration with environment variable support
// Priority order:
// 1. SERVER_API_URL environment variable (highest priority)
// 2. NODE_ENV based configuration
// 3. Default empty string (relative to current context)
SERVER_API_URL: (() => {
  // Check for explicit SERVER_API_URL environment variable
  if (process.env.SERVER_API_URL) {
    return process.env.SERVER_API_URL;
  }
  // Check for NODE_ENV based configuration
  const nodeEnv = process.env.NODE_ENV || 'development';
  
  switch (nodeEnv) {
    case 'production':
      return process.env.PROD_API_URL || '';
    case 'development':
      return process.env.DEV_API_URL || 'http://localhost:8080/';
    case 'test':
      return process.env.TEST_API_URL || 'http://localhost:8080/';
    case 'cloud':
      return process.env.CLOUD_API_URL || 'https://super-broccoli-pj96jxxr4p7q3945r-8080.app.github.dev/';
    default:
      return process.env.DEFAULT_API_URL || 'http://localhost:8080/';
  }
})(),
  // The root URL for the WebSocket, ending with a '/' - for example: `"https://www.jhipster.tech:8081/myservice/"`.
  MONGODB_URI='mongodb+srv://Admin:Admin_1234@cluster0.bfpk1jw.mongodb.net/warehoure?retryWrites=true&w=majority&appName=Cluster0&tls=true'
};
