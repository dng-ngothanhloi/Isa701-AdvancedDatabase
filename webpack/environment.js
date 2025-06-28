module.exports = {
  // APP_VERSION is passed as an environment variable from the Gradle / Maven build tasks.
  VERSION: process.env.APP_VERSION || 'DEV',
  
  // Helper function to get Codespace URL
  getCodespaceUrl: (protocol = 'https') => {
    const codespaceId = process.env.CODESPACE_ID || process.env.CODESPACE_NAME || 'super-broccoli-pj96jxxr4p7q3945r';
    return `${protocol}://${codespaceId}-8080.app.github.dev/`;
  },
  
  // The root URL for API calls, ending with a '/' - for example: `"https://www.jhipster.tech:8081/myservice/"`.
  // If this URL is left empty (""), then it will be relative to the current context.
  // If you use an API server, in `prod` mode, you will need to enable CORS
  // (see the `jhipster.cors` common JHipster property in the `application-*.yml` configurations)
  SERVER_API_URL: (() => {
    // Check for explicit SERVER_API_URL environment variable (highest priority)
    if (process.env.SERVER_API_URL) {
      return process.env.SERVER_API_URL;
    }
    
    // Check for NODE_ENV and SPRING_PROFILES_ACTIVE based configuration
    const nodeEnv = process.env.NODE_ENV || 'development';
    const springProfile = process.env.SPRING_PROFILES_ACTIVE || '';
    
    // Cloud environment (GitHub Codespaces, etc.)
    if (springProfile.includes('cloud')) {
      return process.env.CLOUD_API_URL || module.exports.getCodespaceUrl('https');
    }
    
    // Production environment (deployed to production servers)
    if (nodeEnv === 'production' && !springProfile.includes('cloud')) {
      return process.env.PROD_API_URL || '';
    }
    
    switch (nodeEnv) {
      case 'production':
        return process.env.PROD_API_URL || '';
      case 'development':
        return process.env.DEV_API_URL || 'http://localhost:8080/';
      case 'test':
        return process.env.TEST_API_URL || 'http://localhost:8080/';
      case 'cloud':
        return process.env.CLOUD_API_URL || module.exports.getCodespaceUrl('https');
      default:
        return process.env.DEFAULT_API_URL || 'http://localhost:8080/';
    }
  })(),
  
  // The root URL for the WebSocket, ending with a '/' - for example: `"https://www.jhipster.tech:8081/myservice/"`.
  // If this URL is left empty (""), then it will be relative to the current context.
  // If you use an API server, in `prod` mode, you will need to enable CORS
  // (see the `jhipster.cors` common JHipster property in the `application-*.yml` configurations)
  SERVER_API_URL_WS: (() => {
    // Check for explicit SERVER_API_URL_WS environment variable (highest priority)
    if (process.env.SERVER_API_URL_WS) {
      return process.env.SERVER_API_URL_WS;
    }
    
    // Check for NODE_ENV and SPRING_PROFILES_ACTIVE based configuration
    const nodeEnv = process.env.NODE_ENV || 'development';
    const springProfile = process.env.SPRING_PROFILES_ACTIVE || '';
    
    // Cloud environment (GitHub Codespaces, etc.)
    if (springProfile.includes('cloud')) {
      return process.env.CLOUD_API_URL_WS || module.exports.getCodespaceUrl('wss');
    }
    
    // Production environment (deployed to production servers)
    if (nodeEnv === 'production' && !springProfile.includes('cloud')) {
      return process.env.PROD_API_URL_WS || '';
    }
    
    switch (nodeEnv) {
      case 'production':
        return process.env.PROD_API_URL_WS || '';
      case 'development':
        return process.env.DEV_API_URL_WS || 'ws://localhost:8080/';
      case 'test':
        return process.env.TEST_API_URL_WS || 'ws://localhost:8080/';
      case 'cloud':
        return process.env.CLOUD_API_URL_WS || module.exports.getCodespaceUrl('wss');
      default:
        return process.env.DEFAULT_API_URL_WS || 'ws://localhost:8080/';
    }
  })(),
  
  // MongoDB URI for cloud deployment
  MONGODB_URI: process.env.MONGODB_URI || 'mongodb+srv://Admin:Admin_1234@cluster0.bfpk1jw.mongodb.net/warehoure?retryWrites=true&w=majority&appName=Cluster0&tls=true'
};
