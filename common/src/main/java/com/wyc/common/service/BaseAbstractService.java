package com.wyc.common.service;

import com.alibaba.druid.util.JdbcUtils;
import com.wyc.common.annotation.Condition;
import com.wyc.common.annotation.Conditions;
import com.wyc.common.annotation.CreateAt;
import com.wyc.common.annotation.UpdateAt;
import com.wyc.common.util.CommonUtil;
import ir.nymph.date.DateTime;
import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.parser.Part;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;


abstract public class BaseAbstractService<T> {


    @Data
    private class ConditionVo{
        private String column;
        private Part.Type type;
        private Object value;
    }

    @Data
    private class SelectPropertyVo{
        private String column;
        private String name;
    }

    @Data
    private class TableVo{
        private String table;
        private List<SelectPropertyVo> selectPropertys;
        private List<ConditionVo> conditions;
    }

    @Getter
    private JpaRepository jpaRepository;
    private Class<T> persistentClass;

    @Autowired
    private DataSource dataSource;

    public BaseAbstractService(JpaRepository jpaRepository){
        this.jpaRepository = jpaRepository;
        this.persistentClass=(Class<T>)getSuperClassGenricType(getClass(), 0);
    }

    public void update(T t)throws Exception{
        Class<?> type = t.getClass();
        Field[] fields = type.getDeclaredFields();
        Field updateAtField = null;

        for(Field field:fields){
            field.setAccessible(true);
            Id id = field.getAnnotation(Id.class);

            UpdateAt updateAt = field.getAnnotation(UpdateAt.class);
            if(CommonUtil.isNotEmpty(updateAt)){
                updateAtField = field;
            }
        }

        if(CommonUtil.isNotEmpty(updateAtField)){
            updateAtField.set(t,new Timestamp(new Date().getTime()));
        }

        this.jpaRepository.save(t);
    }

    public <P> P findOne(Class<P> type,String sql){
        List<P> all = findAll(type,sql);
        if(all.size()==1){
            return all.get(0);
        }else if(all.size()>1){
            throw new RuntimeException("有多条记录");
        } else{
            return null;
        }
    }

    public <P> List<P> findAll(Class<P> type,String sql){
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            List<P> targets = new ArrayList<>();
            List<Map<String, Object>> list = JdbcUtils.executeQuery(dataSource, sql);
            for(Map<String,Object> entry:list){
                P target = type.newInstance();
                for(Map.Entry<String,Object> map:entry.entrySet()){
                    try {
                        Field field = type.getDeclaredField(map.getKey());
                        if (CommonUtil.isNotEmpty(field)) {
                            field.setAccessible(true);
                            if(field.getType().equals(int.class)||field.getType().equals(Integer.class)){
                                field.set(target, Integer.valueOf(map.getValue().toString()));
                            }else if(field.getType().equals(long.class)||field.getType().equals(Long.class)){
                                field.set(target, Long.valueOf(map.getValue().toString()));
                            }else if(field.getType().equals(String.class)){
                                field.set(target,map.getValue().toString());
                            }else if(field.getType().equals(Timestamp.class)){
                                try {
                                    Date date = sf.parse(map.getValue().toString());
                                    Timestamp t1 = new Timestamp(date.getTime());
                                    field.set(target,t1);
                                }catch (Exception e){
                                    long lt = new Long(map.getValue().toString());
                                    Timestamp t1 = new Timestamp(lt);
                                    field.set(target,t1);
                                }
                            }else if(field.getType().equals(BigDecimal.class)){
                                if(CommonUtil.isNotEmpty(map.getValue())) {
                                    field.set(target, new BigDecimal(map.getValue().toString()));
                                }
                            }
                        }
                    }catch (Exception e2){

                    }
                }

                targets.add(target);
            }

            return targets;
        }catch (Exception e){
            throw new RuntimeException("发生错误");
        }
    }

    public void add(T t){
        try {
            Class<?> type = t.getClass();
            Field[] fields = type.getDeclaredFields();
            Field idField = null;

            Field createAtField = null;

            Field updateAtField = null;

            for (Field field : fields) {
                Id id = field.getAnnotation(Id.class);
                if (CommonUtil.isNotEmpty(id)) {
                    idField = field;
                }

                CreateAt createAt = field.getAnnotation(CreateAt.class);
                if (CommonUtil.isNotEmpty(createAt)) {
                    createAtField = field;
                }

                UpdateAt updateAt = field.getAnnotation(UpdateAt.class);
                if (CommonUtil.isNotEmpty(updateAt)) {
                    updateAtField = field;
                }
            }
            idField.setAccessible(true);
            idField.set(t, UUID.randomUUID().toString());

            if (CommonUtil.isNotEmpty(createAtField)) {
                createAtField.setAccessible(true);
                createAtField.set(t, new Timestamp(new Date().getTime()));
            }

            if (CommonUtil.isNotEmpty(updateAtField)) {
                updateAtField.setAccessible(true);
                updateAtField.set(t, new Timestamp(new Date().getTime()));
            }

            this.jpaRepository.save(t);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Object getValue(String sql){
        try {
            List<Map<String, Object>> list = JdbcUtils.executeQuery(dataSource, sql);
            if(list.size()>0){
                Map<String,Object> map = list.get(0);
                for(Map.Entry<String,Object> entry:map.entrySet()){
                    return entry.getValue();
                }
            }
        }catch (Exception e){

        }
        return null;
    }

    public T findMax(String name)throws Exception{
        Field[] fields = this.persistentClass.getDeclaredFields();
        Field maxField = null;
        String column = null;
        for(Field field:fields){
            if(field.getName().equals(name)){
                maxField = field;
                Column fieldColumn = field.getAnnotation(Column.class);
                if(CommonUtil.isNotEmpty(fieldColumn)){
                    column = fieldColumn.name();
                }

                if(CommonUtil.isEmpty(column)){
                    column = field.getName().toLowerCase();
                }
                break;
            }
        }

        List<SelectPropertyVo> selectPropertys = selectPropertys();
        TableVo tableVo = table(this.persistentClass.newInstance());
        StringBuffer sb = new StringBuffer();

        sb.append("select ");

        for(SelectPropertyVo selectProperty:tableVo.getSelectPropertys()){
            sb.append(selectProperty.column);
            sb.append(",");
        }

        sb.deleteCharAt(sb.lastIndexOf(","));

        sb.append(" from ");
        sb.append(tableVo.getTable());
        sb.append(" where ");

        sb.append(column+"=");

        sb.append("(select max("+column +") from "+tableVo.getTable()+")");

        List<Map<String, Object>> list = JdbcUtils.executeQuery(dataSource, sb.toString());

        List<T> results = new ArrayList<>();
        for(Map<String,Object> record:list){
            T target = this.persistentClass.newInstance();
            for(SelectPropertyVo selectProperty:selectPropertys){
                String name1 = selectProperty.getName();
                Field field = this.persistentClass.getDeclaredField(name1);
                field.setAccessible(true);
                field.set(target,record.get(selectProperty.getColumn()));
            }
            results.add(target);
        }

        if(results.size()>0) {
            return results.get(0);
        }else{
            return null;
        }

    }


    public T findOne(String id){
        return (T)jpaRepository.findOne(id);
    }

    public T findOne(T t){
        try {
            List<T> all = findAll(t);
            if(CommonUtil.isEmpty(all)||all.size()==0){
                return null;
            }
            return all.get(0);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<T> findAll(T t){
        return findAll(t,null);
    }

    public List<T> findAll(){
        return getJpaRepository().findAll();
    }

    public List<T> findAll(T t, Pageable pageable){

        try {
            TableVo tableVo = table(t);
            StringBuffer sb = new StringBuffer();

            sb.append("select ");

            for (SelectPropertyVo selectProperty : tableVo.getSelectPropertys()) {
                sb.append(selectProperty.column);
                sb.append(",");
            }

            sb.deleteCharAt(sb.lastIndexOf(","));

            sb.append(" from ");
            sb.append(tableVo.getTable());
            sb.append(" where ");

            int index = 0;
            for (ConditionVo conditionVo : tableVo.getConditions()) {
                index++;
                sb.append(conditionSql(conditionVo) + " ");
                if (index < tableVo.getConditions().size()) {
                    sb.append("and ");
                }
            }

            if (CommonUtil.isNotEmpty(pageable)) {
                int pageNumber = pageable.getPageNumber();
                int pageSize = pageable.getPageSize();
                int offset = pageable.getOffset();
                Sort sort = pageable.getSort();
                if (CommonUtil.isNotEmpty(sort)) {
                    Iterator<Sort.Order> orderIterator = sort.iterator();
                    if (orderIterator.hasNext()) {
                        sb.append(" order by ");
                    }
                    while (orderIterator.hasNext()) {
                        Sort.Order order = orderIterator.next();
                        sb.append(order.getProperty());
                        sb.append(" ");
                        sb.append(order.getDirection().toString());
                        sb.append(" ");
                    }
                }
                sb.append(" limit ");
                sb.append(offset);
                sb.append(",");
                sb.append(offset + (pageNumber + 1) * pageSize);

            }

            List<Map<String, Object>> list = JdbcUtils.executeQuery(dataSource, sb.toString());

            List<SelectPropertyVo> selectPropertys = selectPropertys();


            List<T> results = new ArrayList<>();
            for (Map<String, Object> record : list) {
                T target = this.persistentClass.newInstance();
                for (SelectPropertyVo selectProperty : selectPropertys) {
                    String name = selectProperty.getName();
                    Field field = this.persistentClass.getDeclaredField(name);
                    field.setAccessible(true);
                    if (CommonUtil.isEmpty(record.get(selectProperty.getColumn()))) {
                        field.set(target, null);
                    } else if (field.getType().equals(Integer.class) || field.getType().equals(int.class)) {
                        field.set(target, Integer.valueOf(record.get(selectProperty.getColumn()) + ""));
                    } else if (field.getType().equals(Long.class) || field.getType().equals(long.class)) {
                        field.set(target, Long.valueOf(record.get(selectProperty.getColumn()) + ""));
                    } else if (field.getType().equals(Float.class) || field.getType().equals(float.class)) {
                        field.set(target, Float.valueOf(record.get(selectProperty.getColumn()) + ""));
                    } else if (field.getType().equals(Boolean.class) || field.getType().equals(boolean.class)) {
                        field.set(target, Boolean.valueOf(record.get(selectProperty.getColumn()) + ""));
                    } else if (field.getType().equals(String.class)) {
                        field.set(target, String.valueOf(record.get(selectProperty.getColumn()) + ""));
                    }
                }
                results.add(target);

            }
            return results;
        }catch (Exception e){
            e.printStackTrace();
        }

        return  null;
    }

    public String conditionSql(ConditionVo condition){
        Part.Type type = condition.getType();
        if(CommonUtil.isEmpty(type)||type.equals(Part.Type.SIMPLE_PROPERTY)){

            String sql = condition.getColumn()+"='"+condition.getValue()+"'";

            return sql;
        }else if(CommonUtil.isEmpty(type)||type.equals(Part.Type.IN)){
            List<Object> list  = (List<Object>) condition.getValue();
            StringBuffer sb = new StringBuffer();
           sb.append(condition.getColumn()+" in");
           sb.append("(");
           for(Object obj:list){
               sb.append("'");
               sb.append(obj);
               sb.append("'");
               sb.append(",");
           }

           if(list.size()>0){
               sb.deleteCharAt(sb.lastIndexOf(","));
           }
           sb.append(")");
           return sb.toString();

        } else if(CommonUtil.isEmpty(type)||type.equals(Part.Type.GREATER_THAN_EQUAL)){
            String sql = condition.getColumn()+">='"+condition.getValue()+"'";
            return  sql;
        }else if(CommonUtil.isEmpty(type)||type.equals(Part.Type.LESS_THAN_EQUAL)){
            String sql = condition.getColumn()+"<='"+condition.getValue()+"'";
            return  sql;
        }else if(CommonUtil.isEmpty(type)||type.equals(Part.Type.GREATER_THAN)){
            String sql = condition.getColumn()+">'"+condition.getValue()+"'";
            return  sql;
        }else if(CommonUtil.isEmpty(type)||type.equals(Part.Type.LESS_THAN)){
            String sql = condition.getColumn()+"<'"+condition.getValue()+"'";
            return  sql;
        }else{
            return "";
        }
    }


    private TableVo table(T t)throws Exception{
        TableVo tableVo = new TableVo();
        Table table = this.persistentClass.getAnnotation(Table.class);
        String name = table.name();
        if(CommonUtil.isEmpty(name)){
            name = this.persistentClass.getSimpleName().toLowerCase();
        }

        tableVo.setTable(name);

        List<ConditionVo> conditionVos = conditions(t);

        tableVo.setConditions(conditionVos);

        List<SelectPropertyVo> selectPropertys = selectPropertys();
        tableVo.setSelectPropertys(selectPropertys);

        return tableVo;

    }

    private List<SelectPropertyVo> selectPropertys()throws Exception{
        Field[] fields = this.persistentClass.getDeclaredFields();
        List<SelectPropertyVo> selectPropertys = new ArrayList<>();
        for(Field field:fields){
            Column column = field.getAnnotation(Column.class);
            if(CommonUtil.isNotEmpty(column)){
                String name = column.name();
                if(CommonUtil.isEmpty(name)){
                    name = field.getName().toLowerCase();
                }else{

                }

                SelectPropertyVo selectProperty = new SelectPropertyVo();
                selectProperty.setColumn(name);
                selectProperty.setName(field.getName());
                selectPropertys.add(selectProperty);
            }
        }

        return selectPropertys;
    }

    private List<ConditionVo> conditions(T t)throws Exception{
        List<ConditionVo> conditionVos = new ArrayList<>();
        Field[] fields = this.persistentClass.getDeclaredFields();
        for(Field field:fields){
            List<Condition> conditions = new ArrayList<>();
            Conditions conditionsAnn = field.getAnnotation(Conditions.class);
            Condition conditionAnn = field.getAnnotation(Condition.class);
            if(CommonUtil.isNotEmpty(conditionsAnn)){
                for(Condition condition:conditionsAnn.value()){
                    conditions.add(condition);
                }
            }

            if(CommonUtil.isNotEmpty(conditionAnn)){
                conditions.add(conditionAnn);
            }

            Column column = field.getAnnotation(Column.class);

            if(CommonUtil.isNotEmpty(column)){
                String name = column.name();
                if(CommonUtil.isEmpty(name)){
                    name = field.getName().toLowerCase();
                }
                for(Condition condition:conditions){
                    String[] properties = condition.properties();
                    if(CommonUtil.isNotEmpty(properties)&&properties.length>0){
                        for(String property:properties){
                            try {
                                Field propField = t.getClass().getDeclaredField(property);
                                propField.setAccessible(true);
                                Object value = propField.get(t);
                                if(CommonUtil.isNotEmpty(value)) {
                                    ConditionVo conditionVo = new ConditionVo();
                                    conditionVo.setColumn(name);
                                    conditionVo.setType(condition.type());
                                    conditionVo.setValue(value);
                                    conditionVos.add(conditionVo);
                                }
                            }catch (Exception e){

                            }
                        }
                    }else{
                        field.setAccessible(true);
                        Object value = field.get(t);
                        if(CommonUtil.isNotEmpty(value)) {
                            ConditionVo conditionVo = new ConditionVo();
                            conditionVo.setColumn(name);
                            conditionVo.setType(condition.type());
                            conditionVo.setValue(value);
                            conditionVos.add(conditionVo);
                        }
                    }

                }
            }
        }

        return conditionVos;
    }

    private Class<Object> getSuperClassGenricType(final Class clazz, final int index) {
        //返回表示此 Class 所表示的实体（类、接口、基本类型或 void）的直接超类的 Type。
        Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }
        //返回表示此类型实际类型参数的 Type 对象的数组。
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (index >= params.length || index < 0) {
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            return Object.class;
        }

        return (Class) params[index];
    }
}
