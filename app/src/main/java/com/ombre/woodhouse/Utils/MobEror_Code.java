package com.ombre.woodhouse.Utils;

/**
 * Created by OMBRE on 2018/5/2.
 */

//SMSSDK的错误码及描述
public class MobEror_Code {
    public String getErorString(int status){
        String details = null;
        switch (status){
            case 400:
                 details="无效请求";
                break;
            case 405:
                details="AppKey为空";
                break;
            case 406:
                details="AppKey错误";
                break;
            case 407:
                details="缺少数据";
                break;
            case 408:
                details="无效的参数";
                break;
            case 418:
                details="内部接口调用失败";
                break;
            case 450:
                details="权限不足";
                break;
            case 454:
                details="数据格式错误";
                break;
            case 455:
                details="签名无效";
                break;
            case 456:
                details="手机号码为空";
                break;
            case 457:
                details="手机号码格式错误";
                break;
            case 458:
                details="手机号码在黑名单中";
                break;
            case 459:
                details="无appKey的控制数据";
                break;
            case 461:
                details="不支持该地区发送短信";
                break;
            case 460:
                details="无权限发送短信";
                break;
            case 462:
                details="每分钟发送次数超限";
                break;
            case 463:
                details="手机号码每天发送次数超限";
                break;
            case 464:
                details="每台手机每天发送次数超限";
                break;
            case 465:
                details="号码在App中每天发送短信的次数超限\n";
                break;
            case 466:
                details="校验的验证码为空";
                break;
            case 467:
                details="校验验证码请求频繁";
                break;
            case 468:
                details="验证码错误";
                break;
            case 469:
                details="未开启web发送短信";
                break;
            case 470:
                details="账户余额不足";
                break;
            case 471:
                details="请求IP错误";
                break;
            case 472:
                details="客户端请求发送短信验证过于频繁";
                break;
            case 473:
                details="服务端根据duid获取平台错误";
                break;
            case 474:
                details="没有打开服务端验证开关";
                break;
            case 475:
                details="appKey的应用信息不存在";
                break;
            case 476:
                details="当前appkey发送短信的数量超过限额";
                break;
            case 477:
                details="当前手机号发送短信的数量超过限额";
                break;
            case 478:
                details="当前手机号在当前应用内发送超过限额 ";
                break;
            case 500:
                details="API使用受限制";
                break;
            case 600:
                details="服务器内部错误";
                break;
            case 601:
                details="短信发送受限";
                break;
            case 602:
                details="无法发送此地区短信 无法发送此地区短信";
                break;
            case 603:
                details="请填写正确的手机号码";
                break;
            case 604:
                details="当前服务暂不支持此国家";
                break;
        }
        return details;
    }
}
