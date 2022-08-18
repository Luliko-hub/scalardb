// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: scalardb.proto

package com.scalar.db.rpc;

public interface MutateConditionOrBuilder extends
    // @@protoc_insertion_point(interface_extends:rpc.MutateCondition)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>.rpc.MutateCondition.Type type = 1;</code>
   * @return The enum numeric value on the wire for type.
   */
  int getTypeValue();
  /**
   * <code>.rpc.MutateCondition.Type type = 1;</code>
   * @return The type.
   */
  com.scalar.db.rpc.MutateCondition.Type getType();

  /**
   * <code>repeated .rpc.ConditionalExpression expressions = 2;</code>
   */
  java.util.List<com.scalar.db.rpc.ConditionalExpression> 
      getExpressionsList();
  /**
   * <code>repeated .rpc.ConditionalExpression expressions = 2;</code>
   */
  com.scalar.db.rpc.ConditionalExpression getExpressions(int index);
  /**
   * <code>repeated .rpc.ConditionalExpression expressions = 2;</code>
   */
  int getExpressionsCount();
  /**
   * <code>repeated .rpc.ConditionalExpression expressions = 2;</code>
   */
  java.util.List<? extends com.scalar.db.rpc.ConditionalExpressionOrBuilder> 
      getExpressionsOrBuilderList();
  /**
   * <code>repeated .rpc.ConditionalExpression expressions = 2;</code>
   */
  com.scalar.db.rpc.ConditionalExpressionOrBuilder getExpressionsOrBuilder(
      int index);
}
