package org.devzen.springmvcbm;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
public class IndexController {

    @RequestMapping(value="/index", method = RequestMethod.GET)
    public String index(@RequestParam Integer count, Model model) {
        List<FooBean> fooList = new ArrayList<FooBean>();
        for (int i = 0; i < count; i++) {
            fooList.add(new FooBean(10 + i, "name" + i, i % 2 == 0));
        }
        model.addAttribute("fooList", fooList);
        return "index";
    }

    @RequestMapping("/index2")
    public String index2(@RequestParam Integer count, Model model) {
        List<FooBean> fooList = new ArrayList<FooBean>();
        for (int i = 0; i < count; i++) {
            fooList.add(new FooBean(10 + i, "name" + i, i % 2 == 0));
        }
        model.addAttribute("fooList", fooList);
        return "index2.ftl";
    }

    @RequestMapping("/index3")
    public @ResponseBody
    List<FooBean> index3(@RequestParam Integer count) throws Exception {
        List<FooBean> fooList = new ArrayList<FooBean>();
        for (int i = 0; i < count; i++) {
            fooList.add(new FooBean(10 + i, "name" + i, i % 2 == 0));
        }

        return fooList;
    }

}
