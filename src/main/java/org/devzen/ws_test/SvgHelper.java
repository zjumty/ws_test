package org.devzen.ws_test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * User: matianyi
 * Date: 15/2/20
 * Time: 下午11:52
 */
public class SvgHelper {
    // 转换dasharray用
    private static final Map<String, Integer[]> RAPHEAL_DASH_ARRAY;

    static {
        Map<String, Integer[]> map = new HashMap<>();
        map.put("", new Integer[]{0});
        map.put("none", new Integer[]{0});
        map.put("-", new Integer[]{3, 1});
        map.put(".", new Integer[]{1, 1});
        map.put("-.", new Integer[]{3, 1, 1, 1});
        map.put("-..", new Integer[]{3, 1, 1, 1, 1, 1});
        map.put(". ", new Integer[]{1, 3});
        map.put("- ", new Integer[]{4, 3});
        map.put("--", new Integer[]{8, 3});
        map.put("- .", new Integer[]{4, 3, 1, 3});
        map.put("--.", new Integer[]{8, 3, 1, 3});
        map.put("--..", new Integer[]{8, 3, 1, 3, 1, 3});
        RAPHEAL_DASH_ARRAY = Collections.unmodifiableMap(map);
    }

    public static final String KEY_TRANSFORM = "transform";
    public static final String KEY_STROKE_DASHARRAY = "stroke-dasharray";
    public static final String KEY_PATH = "path";
    public static final String KEY_STROKE_WIDTH = "stroke-width";
    public static final String KEY_STROKE_LINECAP = "stroke-linecap";

    /**
     * 把json格式的svg数据转换为
     *
     * @param jsonData json格式的svg数据
     * @return SVG格式的XML
     * @throws XMLStreamException
     */
    public static String buildSvgFromJson(String jsonData) throws XMLStreamException {
        JSONObject body = JSON.parseObject(jsonData);

        StringWriter sw = new StringWriter();
        XMLOutputFactory xof = XMLOutputFactory.newInstance();
        XMLStreamWriter xsw = xof.createXMLStreamWriter(sw);
        xsw.writeStartDocument("utf-8", "1.0");
        xsw.setDefaultNamespace("http://www.w3.org/2000/svg");
        xsw.writeStartElement("svg");
        xsw.writeDefaultNamespace("http://www.w3.org/2000/svg");
        xsw.writeAttribute("version", "1.1");
        addAttributes(xsw, body.getJSONObject("attrs"));

        JSONArray elems = body.getJSONArray("elements");

        for (int i = 0; i < elems.size(); i++) {
            JSONObject elem = elems.getJSONObject(i);
            xsw.writeStartElement(elem.getString("type"));
            addAttributes(xsw, elem.getJSONObject("attrs"));
            xsw.writeEndElement();
        }
        xsw.writeEndElement();
        xsw.writeEndDocument();
        return sw.toString();
    }

    /**
     * 添加属性
     *
     * @param xsw   XML的Writer
     * @param attrs 属性数据对象
     * @throws XMLStreamException
     */
    private static void addAttributes(XMLStreamWriter xsw, JSONObject attrs) throws XMLStreamException {
        for (String key : attrs.keySet()) {
            if (KEY_TRANSFORM.equals(key)) {
                // TODO: 转换transform
                continue;
            } else if (KEY_STROKE_DASHARRAY.equals(key)) {
                xsw.writeAttribute(key, convertDashArray(attrs.getString(key), attrs));
            } else if (KEY_PATH.equals(key)) {
                xsw.writeAttribute("d", convertPath(attrs.getJSONArray(key)));
            } else {
                xsw.writeAttribute(key, attrs.getString(key));
            }
        }
    }

    /**
     * 转换path
     *
     * @param paths 从JSON得到的Path是数组
     * @return M100, 100L150, 60
     */
    private static String convertPath(JSONArray paths) {
        if (paths == null || paths.size() == 0) {
            return "";
        }
        //[["M",100,100],["L",150,60]]
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < paths.size(); i++) {
            JSONArray array = paths.getJSONArray(i);
            sb.append(array.getString(0));
            for (int j = 1; j < array.size(); j++) {
                sb.append(array.get(j));
                if (j != array.size() - 1) {
                    sb.append(",");
                }
            }
        }
        return sb.toString();
    }

    /**
     * 把Rapheal的dasharray的格式转换为标准的svg的格式
     *
     * @param expr  Rapheal的表达式
     * @param attrs 属性对象
     * @return 标准的svg的格式
     */
    private static String convertDashArray(String expr, JSONObject attrs) {
        Integer[] values = RAPHEAL_DASH_ARRAY.get(expr);
        if (values == null) {
            return "";
        }

        int width = attrs.containsKey(KEY_STROKE_WIDTH) ? attrs.getInteger(KEY_STROKE_WIDTH) : 1;
        int butt = 0;
        if ("round".equals(attrs.getString(KEY_STROKE_LINECAP)) || "square".equals(attrs.getString(KEY_STROKE_LINECAP))) {
            butt = width;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            sb.append(values[i] * width + ((i % 2) != 0 ? 1 : -1) * butt);
            if (i != values.length - 1) {
                sb.append(",");
            }
        }

        return sb.toString();
    }
}
