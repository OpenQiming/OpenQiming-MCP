var tips = `## unionpay-mcp-server
unionpay-mcp-server是银联基于MCP协议为AI智能体提供的支付工具（MCP Server），各类支持MCP协议的智能体应用均可安全、便捷地接入银联支付能力。以下是一个行程规划智能体为用户提供行程规划和酒店预定的示例： 传统方式下，用户需要自主查询酒店、对比酒店价格，并完成预订和支付。 智能体模式下，用户只需在智能体应用中输入出行需求（如出发地、目的地、出行时间、预算偏好等），智能体即可自动分析需求，并推荐最佳酒店选择方案，用户确认后，智能体通过银联MCP Server生成支付订单，待用户支付后，智能体完成线上酒店预订操作，并将订单信息同步给用户。整个流程无需人工反复查询和操作，高效便捷。
---
### 可用工具
- **create-contract-order-unionpay-payment** – 创建一笔签约支付订单，并返回授权签约链接。。  
  - \`orderId\` *(字符串, 必需)*：交易订单号,格式:8至40位字母数字  
  - \`txnTime\` *(布尔值, 可选)*：交易时间,格式
---
### 安装
#### 使用 uv（推荐）
当使用 \`uv\` 时无需额外安装，可直接运行：
\`\`\`bash
uvx unionpay-mcp-server
#### 使用 PIP
通过 pip 安装：
\`\`\`bash
pip install unionpay-mcp-server
\`\`\`
安装后脚本运行：
\`\`\`bash
python -m unionpay-mcp-server
\`\`\`
---
### 使用和配置
使用前，需先注册为银联机构服务，并开通相关权限、获取密钥和私钥。之后即可在支持 MCP 的客户端或工具端读取 **MCP Server** 中的支付工具。
#### 在 Cursor 中使用
在 Cursor 项目的 \`.cursor/mcp.json\` 中加入如下配置：

\`\`\`json
{
  "mcpServers": {
    "unionpay-mcp-server": {
      "command": "python",
      "args": [
        "-m",
        "unionpay-mcp-server"
      ],
      "env": {
        "CUP_ACQS_CODE": "中付商户, 长度固定 8 位",
        "CUP_AVAILABLE_TOOLS": "仅允许白名单工具, 多值用逗号分隔, 如 create-contract-order,unionpay-payment,create-contract-unionpay-payment"
      }
    }
  }
}
\`\`\`

`;



export default tips;