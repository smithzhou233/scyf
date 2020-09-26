package com.hngf.common.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeUtils<T> {
    private Integer id;           // 节点id
    private Integer parentId;    // 父节点
    private String label;         // 节点名称 ,返回给前台的是一个装有TreeUtils的集合的数据，所以在前台显示数据的时候，el-tree的lable的名字的和这个一样
    private List<TreeUtils> children;  // 父节点中存放子节点的集合
    //private T data;              //  节点数据

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<TreeUtils> getChildren() {
        return children;
    }

    public void setChildren(List<TreeUtils> children) {
        this.children = children;
    }

    /* public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }*/


    /**
     * @param listData   // 从数据库中查询的数据
     * @return
     */
    public static List<TreeUtils> getTreeList(List<?> listData ,String groupId,String groupParent,String groupName) throws Exception{

        List<TreeUtils> resultList = new ArrayList<TreeUtils>();  // 最终返回的结果
        Map<Integer ,Object> map = new HashMap<Integer,Object>();

        for(int i =0;i<listData.size() && !listData.isEmpty();i++){

            // 写一个与该方法差不多的方法，将得到TreeUtils的代码抽取出来
            // 也可以将listData集合整个转换成装有TreeUtils的集合x，然后再循环x
            TreeUtils treeUtils = new TreeUtils();
            treeUtils.setId(Integer.parseInt(TreeUtils.getFileValue(listData.get(i),groupId).toString())); // id              // 返回值为Object无法直接转换成Integer,先toString，再转换成Integer。这里的返回值写成Object是因为多种类型字段的值都可以用该方法
            treeUtils.setParentId(Integer.parseInt(TreeUtils.getFileValue(listData.get(i),groupParent).toString())); // 父id
            treeUtils.setLabel(TreeUtils.getFileValue(listData.get(i),groupName).toString());  // 节点名
            //System.out.println("节点名为+"+TreeUtils.getFileValue(listData.get(i),categoryName).toString());
            //treeUtils.setData(listData.get(i));   // data:原对象中的所有属性，无children

            // 通过反射得到每条数据的id将数据封装的map集合中，id为键，元素本身为值
            map.put(treeUtils.getId(),treeUtils);


            // 将所有顶层元素添加到resultList集合中
            //if( 0 == treeUtils.getParentId()){
            //   resultList.add(treeUtils);
            // }
        }
// 得到所有的顶层节点，游离节点也算作顶层节点
// 优点一，不用知道等级节点的id
// 优点而，只查询了一次数据库
        for(int i =0;i<listData.size();i++){
            if(!map.containsKey(Integer.parseInt(TreeUtils.getFileValue(listData.get(i),groupParent).toString()))){
                resultList.add((TreeUtils) map.get(Integer.parseInt(TreeUtils.getFileValue(listData.get(i),groupId).toString())));
            }
        }



        for(int i =0;i<listData.size() && !listData.isEmpty();i++){
            TreeUtils obj = (TreeUtils)map.get(Integer.parseInt(TreeUtils.getFileValue(listData.get(i), groupParent).toString()));
            if(obj != null){
                if(obj.getChildren() == null){
                    String[] s=new String[0];
                    obj.setChildren(new ArrayList());
                }
                obj.getChildren().add(map.get(Integer.parseInt(TreeUtils.getFileValue(listData.get(i),groupId).toString())));
            }
        }
        return resultList;
    }
    /**
     * 通过反射得到的数据类型的也是不一定的，所以这里我们返回值为Object
     * Object是无法直接转为Integer,现将Object转为String，然后再将String转为Integer
     * @param item
     * @param fileName
     * @return
     */
    public static Object  getFileValue(Object item,String fileName) throws Exception {
        Class<?> aClass = item.getClass();
        Field file =  aClass.getDeclaredField(fileName); // 得到所有字段包括私有字段
        file.setAccessible(true); // 取消访问限制
        return file.get(item);    // 这里就体现出反射的意思了，我们通常都是通过对象拿到字段，这里是通过字段，将类的字节码对象为参数传入，来得到值
    }
}
