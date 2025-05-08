package com.tu.mall.template;

import java.util.Arrays;
import java.util.Collections;

import cn.hutool.json.JSONUtil;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversation;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversationParam;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversationResult;
import com.alibaba.dashscope.common.MultiModalMessage;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.exception.UploadFileException;
import com.alibaba.dashscope.utils.JsonUtils;
import com.tu.mall.properties.ChatProperties;

import java.util.Arrays;
import java.util.Collections;

/**
 * @author JiFeiYe
 * @since 2025/2/20
 */
public class ChatTemplate {
    private final ChatProperties chatProperties;

    public ChatTemplate(ChatProperties chatProperties) {
        this.chatProperties = chatProperties;
    }

    public void generationResult() throws NoApiKeyException, UploadFileException {
        MultiModalConversation conv = new MultiModalConversation();
        MultiModalMessage userMessage = MultiModalMessage.builder().role(Role.USER.getValue())
                .content(Arrays.asList(
                        Collections.singletonMap("image", "https://ll-secondmall.oss-cn-shenzhen.aliyuncs.com/2025/02/26/Trainingdata_vl/image_1.jpeg"),
//                        Collections.singletonMap("image", "https://ll-secondmall.oss-cn-shenzhen.aliyuncs.com/2024/11/13/003d0ae9-d5fe-4559-be92-a217dbbb57f9.jpg"),
//                        Collections.singletonMap("image", "https://ll-secondmall.oss-cn-shenzhen.aliyuncs.com/2024/12/22/b2e5baf7-ce46-4493-8686-78699693f2fe.jpg"),
//                        Collections.singletonMap("image", "https://dashscope.oss-cn-beijing.aliyuncs.com/images/dog_and_girl.jpeg"),
//                        Collections.singletonMap("image", "https://dashscope.oss-cn-beijing.aliyuncs.com/images/tiger.png"),
//                        Collections.singletonMap("image", "https://dashscope.oss-cn-beijing.aliyuncs.com/images/rabbit.png"),
                        Collections.singletonMap("text", "这张图片的内容是什么，预计价值多少？"))).build();
        MultiModalConversationParam param = MultiModalConversationParam.builder()
                // 若没有配置环境变量，请用百炼API Key将下行替换为：.apiKey("sk-xxx")
                .apiKey(chatProperties.getApiKey())
//                .apiKey(System.getenv("sk-5d75ce6907974e328a2196c74e76ed18"))
                // 此处以qwen-vl-plus为例，可按需更换模型名称。模型列表：https://help.aliyun.com/zh/model-studio/getting-started/models
                .model("qwen-vl-plus")
                .message(userMessage)
                .build();
        MultiModalConversationResult result = conv.call(param);
        System.out.println("JSONUtil.formatJsonStr(JSONUtil.toJsonStr(result)) = \n" + JSONUtil.formatJsonStr(JSONUtil.toJsonStr(result)));
//        System.out.println(JsonUtils.toJson(result));
    }
}
