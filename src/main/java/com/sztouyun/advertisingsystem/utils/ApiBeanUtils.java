package com.sztouyun.advertisingsystem.utils;

import com.sztouyun.advertisingsystem.common.ITree;
import com.sztouyun.advertisingsystem.common.ITreeList;
import com.sztouyun.advertisingsystem.viewmodel.common.PageList;
import org.apache.calcite.linq4j.Enumerable;
import org.apache.calcite.linq4j.Linq4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class ApiBeanUtils {

    public static <S,T> PageList<T> convertToPageList(Page<S> pages,Function<S, T> mapper){
        PageList<T> pageList=new PageList<>();
        pageList.setTotalElement(pages.getTotalElements());
        pageList.setTotalPageSize(pages.getTotalPages());
        pageList.setPageIndex(pages.getNumber());
        pageList.setPageSize(pages.getSize());
        pageList.setList(Linq4j.asEnumerable(pages.getContent()).select(mapper::apply).toList());
        return pageList;
    }

    public static <S extends ITree<V>,T extends ITreeList<T>,V> List<T> convertToTreeList(Iterable<S> dataList, Function<S, T> mapper, V rootId){
        if(dataList ==null)
            return new ArrayList<>();

        Enumerable<S> list=  Linq4j.asEnumerable(dataList);
        List<T> children = getChildren(list,rootId,mapper);
        S rootItem = list.firstOrDefault(l->l.getId().equals(rootId));
        if(rootItem == null)
            return children;

        T targetItem = mapper.apply(rootItem);
        targetItem.setChildren(children);
        return Collections.singletonList(targetItem);
    }

    public static <S,T> T copyProperties(S source ,Class<T> targetClass){
        try {
            T target =targetClass.newInstance();
            BeanUtils.copyProperties(source,target);
            return target;
        }catch (Exception ex){
            return null;
        }
    }

    private static <S extends ITree<V>,T extends ITreeList<T>,V> List<T> getChildren(Enumerable<S> list,V parenId,Function<S, T> mapper){
     return list.where(l-> parenId.equals( l.getParentId())).select(item->{
            T viewModel =mapper.apply(item);
            viewModel.setChildren(getChildren(list,item.getId(),mapper));
            return viewModel;
        }).toList();
    }
}
