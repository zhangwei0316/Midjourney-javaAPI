package com.qjc.midjourney.controller;

import com.alibaba.fastjson.JSONObject;
import com.qjc.midjourney.dto.RequestTrigger;
import com.qjc.midjourney.result.ResponseData;
import com.qjc.midjourney.service.MidjourneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/midjourney")
public class MidjourneyController {

    private final MidjourneyService midjourneyService;

    @Autowired
    public MidjourneyController(MidjourneyService midjourneyService) {
        this.midjourneyService = midjourneyService;
    }

    @PostMapping
    public ResponseEntity<ResponseData> midjourneyBot(@RequestBody RequestTrigger requestTrigger) {
        try {
            String res = midjourneyService.executeAction(requestTrigger);
            return ResponseEntity.ok(ResponseData.builder()
                    .message(res)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(ResponseData.builder()
                    .code(400)
                    .error(e.getMessage())
                    .build());
        }
    }

    @GetMapping("detail")
    public ResponseEntity<ResponseData> detail(@RequestParam String id) {
        try {
            String res = midjourneyService.detail(id);
            return ResponseEntity.ok(ResponseData.builder()
                    .message(res)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(ResponseData.builder()
                    .code(400)
                    .error(e.getMessage())
                    .build());
        }
    }

    @PostMapping("sync/user")
    public ResponseEntity<ResponseData> syncUser(@RequestBody JSONObject data) {
        try {
            String res = midjourneyService.syncUser(data);
            return ResponseEntity.ok(ResponseData.builder()
                    .message(res)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(ResponseData.builder()
                    .code(400)
                    .error(e.getMessage())
                    .build());
        }
    }
}