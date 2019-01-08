//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.exception;

import java.util.HashMap;
import java.util.Map;

public class ExceptionMsgUtils {
    private static final Map<Integer, String> msgList = new HashMap();

    public ExceptionMsgUtils() {
    }

    public static String getMessage(int errorCode) {
        String msg = (String)msgList.get(errorCode);
        return msg != null ? msg : String.valueOf(errorCode);
    }

    static {
        msgList.put(99999999, "SUCCESS:业务处理成功");
        msgList.put(99999998, "DIS_TX_HAS_HANDLE_SUCCESS:业务已经处理成功");
        msgList.put(99001001, "UNKNOW_SYSTEM_EXCEPTION:未知的系统错误");
        msgList.put(99002001, "SERVICE_MORE_THAN_1_WHEN_COMMIT:有且只能有一个form提交service被配置");
        msgList.put(99001006, "POLL_SERVICE_SYSTEM_EXCEPTION:未知的系统错误");
        msgList.put(99001002, "SERVICE_NOT_EXIST:服务不存在");
        msgList.put(99001003, "REMOTE_SERVER_IS_DOWN:远程服务器没有启动");
        msgList.put(99001004, "PARTNER_NO_AUTHCODE:合作伙伴没有授权代码");
        msgList.put(99001005, "OPEN_API_SIGN_ERROR:开放api签名错误");
        msgList.put(99001007, "SERVICE_DATA_NOT_COMPLETED:服务层返回数据不完整");
        msgList.put(99001050, "DATA_VAL_ERROR:数据验证错误");
        msgList.put(99100001, "VAL_IS_NULL_ERROR");
        msgList.put(99100003, "VAL_EMAIL_FORMAT_ERROR");
        msgList.put(99100006, "VAL_MAX_LENGTH_ERROR");
        msgList.put(99100009, "VAL_MIN_LENGTH_ERROR");
        msgList.put(99100013, "VAL_IS_BLANK_ERROR");
        msgList.put(99100016, "VAL_MAX_RANGE_ERROR");
        msgList.put(99100019, "VAL_MIN_RANGE_ERROR");
        msgList.put(99100022, "VAL_MOBILE_FORMAT_ERROR");
        msgList.put(99100028, "VAL_URL_FORMAT_ERROR");
        msgList.put(99100024, "VAL_NOT_INT_ERROR:不是有效的整数");
        msgList.put(99100025, "VAL_NOT_NUMBER_ERROR:不是有效的数字");
        msgList.put(99100026, "VAL_NOT_MONEY_ERROR:不是一个有效的货币值");
        msgList.put(99101010, "VAL_NULL_PARAMETERS:用户提交验证的参数是NULL");
        msgList.put(99101011, "VAL_PARAMETERS_LENGTH_INVALIDE:参数长度不正确");
        msgList.put(99101021, "VAL_TOKEN_UNEXIST:TOKEN不存在");
        msgList.put(99101022, "VAL_TOKEN_HANDING:请求正在处理中");
        msgList.put(99101023, "VAL_TOKEN_HANDED:请求已经处理成功");
        msgList.put(99101033, "VAL_FORM_SIGN_ERROR:签名串不正确");
        msgList.put(99201001, "ACCESS_CONTROL_USER_IS_NULL:有产品代码，但用户是NULL");
        msgList.put(99201002, "ACCESS_CONTROL_NO_PROVIDER:没有提供实现者");
        msgList.put(99201003, "ACCESS_CONTROL_NO_ROLE:没有匹配的角色");
        msgList.put(99201004, "NOOTIFY_FAILURE:通知发送失败");
        msgList.put(99201005, "NOTIFY_OVER_TIMEWINDOW:通知-超过时间窗口");
        msgList.put(99201006, "NOTIFY_NEWWORK_EXCEPTION:通知-网络异常");
        msgList.put(99201007, "NOTIFY_IS_DEAD:通知-event is dead");
        msgList.put(99201008, "NOTIFY_IS_NEW:事务性事件还没有提交");
        msgList.put(97100001, "FS_MANAGER_IS_NOT_INITED:文件数据存储器没有初始化");
        msgList.put(97100002, "QUEUE_NAME_IS_NULL:Queue的name是null");
        msgList.put(97100003, "ASYNC_GET_RESULT_EXCEPTION:异步获得结果时发生异常");
        msgList.put(97100004, "NO_LISTENER_EXCEPTION:事件没有对应的响应者");
    }
}
