package com.jzz.learn.utils.deferredresult;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author jzz
 * @date 2019/5/17
 */
@RestController
@RequestMapping("/task")
@Slf4j
public class TaskController {

    private Map<String, DeferredResult<Map<String,Object>>> deferredResultMap = new HashMap<>();
    private static Map<String,Object> defaultMap = new HashMap<>();
    static {
        defaultMap.put("3",true);
    }
    @GetMapping("/result")
    public DeferredResult<Map<String,Object>> result(){
        log.info("result start");
        DeferredResult<Map<String,Object>> deferredResult = new  DeferredResult<>(20_000L,defaultMap);
        deferredResultMap.put("1",deferredResult);
        deferredResult.onCompletion(()->{
            Map<String,Object> map = (Map<String,Object>)deferredResult.getResult();
            map.put("1",true);
            if(map.containsKey("2")){
                log.info("222222222222222222222222222222222222");
            }
        });
        return deferredResult;
    }
    @GetMapping("/result2")
    public void result2(){
        DeferredResult<Map<String, Object>> deferredResult = deferredResultMap.get("1");
        Map<String, Object> map =  new HashMap<>(16);
        map.put("2",true);
        deferredResult.setResult(map);
    }
}
