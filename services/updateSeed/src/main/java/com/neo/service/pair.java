package com.neo.service;

public class pair {
  public int par;
  public int val;

  public pair(int par, int val){
    this.par = par;
    this.val = val;
  }

  @Override
  public boolean equals(Object anObject){
    if(this == anObject)  // 引用相等
      return true;
    if(anObject == null)  // 对象为null
      return false;
    if(getClass() !=anObject.getClass()) //类是否相同
      return false;
    pair another = (pair) anObject;
    return par == another.par && val == another.val;  // 域属性相等
  }
}
