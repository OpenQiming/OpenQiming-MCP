const config = {};

const enviroment = process.env.enviroment;

if (enviroment === 'production') {
    config.gateUrl = 'http://****:8090'; //mcp-server接口
} else {
    console.log('本地环境', window.location);
    config.gateUrl = 'http://localhost:9001'; //mcp-server接口
}

config.env = enviroment;

export default config;
