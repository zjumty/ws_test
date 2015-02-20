package org.devzen.ws_test;

import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.StringReader;
import java.util.*;

@Controller
@RequestMapping("/")
public class IndexController {

    @RequestMapping(value = "/")
    public String home() {
        return "quote.ftl";
    }

    @RequestMapping(value = "/svg", method = RequestMethod.GET)
    public String showSvg() {
        return "svg.ftl";
    }

    @RequestMapping(value = "/svg", method = RequestMethod.POST)
    public void downloadSvg(@RequestParam("svg") String body, @RequestParam("type") String type, HttpServletResponse resp) throws Exception {
        if (body == null || body.equals("")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "图像内容为空");
            return;
        }

        if ("svg".equals(type)) {
            TranscoderInput input = new TranscoderInput(new StringReader(body));
            TranscoderOutput output = new TranscoderOutput(resp.getOutputStream());

            resp.setContentType("image/png");
            PNGTranscoder converter = new PNGTranscoder();
            long s = System.currentTimeMillis();
            converter.transcode(input, output);
            long e = System.currentTimeMillis();
            System.out.println("time:" + (e - s) + "ms");
        } else if ("vml".equals(type)) {

        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "图像类型必须是SVG或VML");
        }
    }

    @RequestMapping(value = "/svg2", method = RequestMethod.POST)
    public void downloadSvg2(@RequestParam("svg") String content, HttpServletResponse resp) throws Exception {
        if (content == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "图像内容为空");
            return;
        }
        long s = System.currentTimeMillis();
        String svg = SvgHelper.buildSvgFromJson(content);

        System.out.println(svg);
        TranscoderInput input = new TranscoderInput(new StringReader(svg));
        TranscoderOutput output = new TranscoderOutput(resp.getOutputStream());

        resp.setContentType("image/png");
        PNGTranscoder converter = new PNGTranscoder();

        converter.transcode(input, output);

        long e = System.currentTimeMillis();
        System.out.println("time:" + (e - s) + "ms");

    }


    @RequestMapping(value = "/index", method = RequestMethod.GET)
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
    public @ResponseBody List<FooBean> index3(@RequestParam Integer count) throws Exception {
        List<FooBean> fooList = new ArrayList<FooBean>();
        for (int i = 0; i < count; i++) {
            fooList.add(new FooBean(10 + i, "name" + i, i % 2 == 0));
        }

        return fooList;
    }

}
