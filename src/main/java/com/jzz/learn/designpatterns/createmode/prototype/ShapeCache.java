package com.jzz.learn.designpatterns.createmode.prototype;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 访问类
 * @author jzz
 * @date 2019/5/21
 */
public class ShapeCache {
    private static Map<String, AbstractShape> shapeMap = new ConcurrentHashMap<>();

    public static AbstractShape getShape(String shapeId) {
        AbstractShape cachedShape = shapeMap.get(shapeId);
        return (AbstractShape) cachedShape.clone();
    }

    /**
     * 对每种形状都运行数据库查询，并创建该形状
     * shapeMap.put(shapeKey, shape);
     * 例如，我们要添加三种形状
     */
    public static void loadCache() {
        Circle circle = new Circle();
        circle.setId("1");
        shapeMap.put(circle.getId(),circle);

        Square square = new Square();
        square.setId("2");
        shapeMap.put(square.getId(),square);

        Rectangle rectangle = new Rectangle();
        rectangle.setId("3");
        shapeMap.put(rectangle.getId(),rectangle);
    }
}
