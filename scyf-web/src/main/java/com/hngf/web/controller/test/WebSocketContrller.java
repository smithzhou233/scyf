package com.hngf.web.controller.test;

import com.hngf.common.utils.R;
import com.hngf.service.utils.SendMessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("ws/websocket")
public class WebSocketContrller {

    @GetMapping("/msg")
    @ResponseBody
    public R sendMsg() {
        SendMessageUtil.sendBigScreen(1L, 6L, "");
        return R.ok();
    }

    @GetMapping("/toWsPage")
    public String toWsPage() {
        return "ws/ws.html";
    }
}
