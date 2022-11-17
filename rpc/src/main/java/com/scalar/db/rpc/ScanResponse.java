// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: scalardb.proto

package com.scalar.db.rpc;

/**
 * Protobuf type {@code rpc.ScanResponse}
 */
public final class ScanResponse extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:rpc.ScanResponse)
    ScanResponseOrBuilder {
private static final long serialVersionUID = 0L;
  // Use ScanResponse.newBuilder() to construct.
  private ScanResponse(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private ScanResponse() {
    result_ = java.util.Collections.emptyList();
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new ScanResponse();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.scalar.db.rpc.ScalarDbProto.internal_static_rpc_ScanResponse_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.scalar.db.rpc.ScalarDbProto.internal_static_rpc_ScanResponse_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.scalar.db.rpc.ScanResponse.class, com.scalar.db.rpc.ScanResponse.Builder.class);
  }

  public static final int RESULT_FIELD_NUMBER = 2;
  private java.util.List<com.scalar.db.rpc.Result> result_;
  /**
   * <code>repeated .rpc.Result result = 2;</code>
   */
  @java.lang.Override
  public java.util.List<com.scalar.db.rpc.Result> getResultList() {
    return result_;
  }
  /**
   * <code>repeated .rpc.Result result = 2;</code>
   */
  @java.lang.Override
  public java.util.List<? extends com.scalar.db.rpc.ResultOrBuilder> 
      getResultOrBuilderList() {
    return result_;
  }
  /**
   * <code>repeated .rpc.Result result = 2;</code>
   */
  @java.lang.Override
  public int getResultCount() {
    return result_.size();
  }
  /**
   * <code>repeated .rpc.Result result = 2;</code>
   */
  @java.lang.Override
  public com.scalar.db.rpc.Result getResult(int index) {
    return result_.get(index);
  }
  /**
   * <code>repeated .rpc.Result result = 2;</code>
   */
  @java.lang.Override
  public com.scalar.db.rpc.ResultOrBuilder getResultOrBuilder(
      int index) {
    return result_.get(index);
  }

  public static final int HAS_MORE_RESULTS_FIELD_NUMBER = 3;
  private boolean hasMoreResults_;
  /**
   * <code>bool has_more_results = 3;</code>
   * @return The hasMoreResults.
   */
  @java.lang.Override
  public boolean getHasMoreResults() {
    return hasMoreResults_;
  }

  private byte memoizedIsInitialized = -1;
  @java.lang.Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @java.lang.Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    for (int i = 0; i < result_.size(); i++) {
      output.writeMessage(2, result_.get(i));
    }
    if (hasMoreResults_ != false) {
      output.writeBool(3, hasMoreResults_);
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    for (int i = 0; i < result_.size(); i++) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(2, result_.get(i));
    }
    if (hasMoreResults_ != false) {
      size += com.google.protobuf.CodedOutputStream
        .computeBoolSize(3, hasMoreResults_);
    }
    size += getUnknownFields().getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof com.scalar.db.rpc.ScanResponse)) {
      return super.equals(obj);
    }
    com.scalar.db.rpc.ScanResponse other = (com.scalar.db.rpc.ScanResponse) obj;

    if (!getResultList()
        .equals(other.getResultList())) return false;
    if (getHasMoreResults()
        != other.getHasMoreResults()) return false;
    if (!getUnknownFields().equals(other.getUnknownFields())) return false;
    return true;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    if (getResultCount() > 0) {
      hash = (37 * hash) + RESULT_FIELD_NUMBER;
      hash = (53 * hash) + getResultList().hashCode();
    }
    hash = (37 * hash) + HAS_MORE_RESULTS_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(
        getHasMoreResults());
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.scalar.db.rpc.ScanResponse parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.scalar.db.rpc.ScanResponse parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.scalar.db.rpc.ScanResponse parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.scalar.db.rpc.ScanResponse parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.scalar.db.rpc.ScanResponse parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.scalar.db.rpc.ScanResponse parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.scalar.db.rpc.ScanResponse parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.scalar.db.rpc.ScanResponse parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.scalar.db.rpc.ScanResponse parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.scalar.db.rpc.ScanResponse parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.scalar.db.rpc.ScanResponse parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.scalar.db.rpc.ScanResponse parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(com.scalar.db.rpc.ScanResponse prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @java.lang.Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code rpc.ScanResponse}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:rpc.ScanResponse)
      com.scalar.db.rpc.ScanResponseOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.scalar.db.rpc.ScalarDbProto.internal_static_rpc_ScanResponse_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.scalar.db.rpc.ScalarDbProto.internal_static_rpc_ScanResponse_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.scalar.db.rpc.ScanResponse.class, com.scalar.db.rpc.ScanResponse.Builder.class);
    }

    // Construct using com.scalar.db.rpc.ScanResponse.newBuilder()
    private Builder() {

    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);

    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      if (resultBuilder_ == null) {
        result_ = java.util.Collections.emptyList();
      } else {
        result_ = null;
        resultBuilder_.clear();
      }
      bitField0_ = (bitField0_ & ~0x00000001);
      hasMoreResults_ = false;

      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.scalar.db.rpc.ScalarDbProto.internal_static_rpc_ScanResponse_descriptor;
    }

    @java.lang.Override
    public com.scalar.db.rpc.ScanResponse getDefaultInstanceForType() {
      return com.scalar.db.rpc.ScanResponse.getDefaultInstance();
    }

    @java.lang.Override
    public com.scalar.db.rpc.ScanResponse build() {
      com.scalar.db.rpc.ScanResponse result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.scalar.db.rpc.ScanResponse buildPartial() {
      com.scalar.db.rpc.ScanResponse result = new com.scalar.db.rpc.ScanResponse(this);
      int from_bitField0_ = bitField0_;
      if (resultBuilder_ == null) {
        if (((bitField0_ & 0x00000001) != 0)) {
          result_ = java.util.Collections.unmodifiableList(result_);
          bitField0_ = (bitField0_ & ~0x00000001);
        }
        result.result_ = result_;
      } else {
        result.result_ = resultBuilder_.build();
      }
      result.hasMoreResults_ = hasMoreResults_;
      onBuilt();
      return result;
    }

    @java.lang.Override
    public Builder clone() {
      return super.clone();
    }
    @java.lang.Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.setField(field, value);
    }
    @java.lang.Override
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return super.clearField(field);
    }
    @java.lang.Override
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return super.clearOneof(oneof);
    }
    @java.lang.Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, java.lang.Object value) {
      return super.setRepeatedField(field, index, value);
    }
    @java.lang.Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.addRepeatedField(field, value);
    }
    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof com.scalar.db.rpc.ScanResponse) {
        return mergeFrom((com.scalar.db.rpc.ScanResponse)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.scalar.db.rpc.ScanResponse other) {
      if (other == com.scalar.db.rpc.ScanResponse.getDefaultInstance()) return this;
      if (resultBuilder_ == null) {
        if (!other.result_.isEmpty()) {
          if (result_.isEmpty()) {
            result_ = other.result_;
            bitField0_ = (bitField0_ & ~0x00000001);
          } else {
            ensureResultIsMutable();
            result_.addAll(other.result_);
          }
          onChanged();
        }
      } else {
        if (!other.result_.isEmpty()) {
          if (resultBuilder_.isEmpty()) {
            resultBuilder_.dispose();
            resultBuilder_ = null;
            result_ = other.result_;
            bitField0_ = (bitField0_ & ~0x00000001);
            resultBuilder_ = 
              com.google.protobuf.GeneratedMessageV3.alwaysUseFieldBuilders ?
                 getResultFieldBuilder() : null;
          } else {
            resultBuilder_.addAllMessages(other.result_);
          }
        }
      }
      if (other.getHasMoreResults() != false) {
        setHasMoreResults(other.getHasMoreResults());
      }
      this.mergeUnknownFields(other.getUnknownFields());
      onChanged();
      return this;
    }

    @java.lang.Override
    public final boolean isInitialized() {
      return true;
    }

    @java.lang.Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      if (extensionRegistry == null) {
        throw new java.lang.NullPointerException();
      }
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            case 18: {
              com.scalar.db.rpc.Result m =
                  input.readMessage(
                      com.scalar.db.rpc.Result.parser(),
                      extensionRegistry);
              if (resultBuilder_ == null) {
                ensureResultIsMutable();
                result_.add(m);
              } else {
                resultBuilder_.addMessage(m);
              }
              break;
            } // case 18
            case 24: {
              hasMoreResults_ = input.readBool();

              break;
            } // case 24
            default: {
              if (!super.parseUnknownField(input, extensionRegistry, tag)) {
                done = true; // was an endgroup tag
              }
              break;
            } // default:
          } // switch (tag)
        } // while (!done)
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.unwrapIOException();
      } finally {
        onChanged();
      } // finally
      return this;
    }
    private int bitField0_;

    private java.util.List<com.scalar.db.rpc.Result> result_ =
      java.util.Collections.emptyList();
    private void ensureResultIsMutable() {
      if (!((bitField0_ & 0x00000001) != 0)) {
        result_ = new java.util.ArrayList<com.scalar.db.rpc.Result>(result_);
        bitField0_ |= 0x00000001;
       }
    }

    private com.google.protobuf.RepeatedFieldBuilderV3<
        com.scalar.db.rpc.Result, com.scalar.db.rpc.Result.Builder, com.scalar.db.rpc.ResultOrBuilder> resultBuilder_;

    /**
     * <code>repeated .rpc.Result result = 2;</code>
     */
    public java.util.List<com.scalar.db.rpc.Result> getResultList() {
      if (resultBuilder_ == null) {
        return java.util.Collections.unmodifiableList(result_);
      } else {
        return resultBuilder_.getMessageList();
      }
    }
    /**
     * <code>repeated .rpc.Result result = 2;</code>
     */
    public int getResultCount() {
      if (resultBuilder_ == null) {
        return result_.size();
      } else {
        return resultBuilder_.getCount();
      }
    }
    /**
     * <code>repeated .rpc.Result result = 2;</code>
     */
    public com.scalar.db.rpc.Result getResult(int index) {
      if (resultBuilder_ == null) {
        return result_.get(index);
      } else {
        return resultBuilder_.getMessage(index);
      }
    }
    /**
     * <code>repeated .rpc.Result result = 2;</code>
     */
    public Builder setResult(
        int index, com.scalar.db.rpc.Result value) {
      if (resultBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureResultIsMutable();
        result_.set(index, value);
        onChanged();
      } else {
        resultBuilder_.setMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .rpc.Result result = 2;</code>
     */
    public Builder setResult(
        int index, com.scalar.db.rpc.Result.Builder builderForValue) {
      if (resultBuilder_ == null) {
        ensureResultIsMutable();
        result_.set(index, builderForValue.build());
        onChanged();
      } else {
        resultBuilder_.setMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .rpc.Result result = 2;</code>
     */
    public Builder addResult(com.scalar.db.rpc.Result value) {
      if (resultBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureResultIsMutable();
        result_.add(value);
        onChanged();
      } else {
        resultBuilder_.addMessage(value);
      }
      return this;
    }
    /**
     * <code>repeated .rpc.Result result = 2;</code>
     */
    public Builder addResult(
        int index, com.scalar.db.rpc.Result value) {
      if (resultBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureResultIsMutable();
        result_.add(index, value);
        onChanged();
      } else {
        resultBuilder_.addMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .rpc.Result result = 2;</code>
     */
    public Builder addResult(
        com.scalar.db.rpc.Result.Builder builderForValue) {
      if (resultBuilder_ == null) {
        ensureResultIsMutable();
        result_.add(builderForValue.build());
        onChanged();
      } else {
        resultBuilder_.addMessage(builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .rpc.Result result = 2;</code>
     */
    public Builder addResult(
        int index, com.scalar.db.rpc.Result.Builder builderForValue) {
      if (resultBuilder_ == null) {
        ensureResultIsMutable();
        result_.add(index, builderForValue.build());
        onChanged();
      } else {
        resultBuilder_.addMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .rpc.Result result = 2;</code>
     */
    public Builder addAllResult(
        java.lang.Iterable<? extends com.scalar.db.rpc.Result> values) {
      if (resultBuilder_ == null) {
        ensureResultIsMutable();
        com.google.protobuf.AbstractMessageLite.Builder.addAll(
            values, result_);
        onChanged();
      } else {
        resultBuilder_.addAllMessages(values);
      }
      return this;
    }
    /**
     * <code>repeated .rpc.Result result = 2;</code>
     */
    public Builder clearResult() {
      if (resultBuilder_ == null) {
        result_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000001);
        onChanged();
      } else {
        resultBuilder_.clear();
      }
      return this;
    }
    /**
     * <code>repeated .rpc.Result result = 2;</code>
     */
    public Builder removeResult(int index) {
      if (resultBuilder_ == null) {
        ensureResultIsMutable();
        result_.remove(index);
        onChanged();
      } else {
        resultBuilder_.remove(index);
      }
      return this;
    }
    /**
     * <code>repeated .rpc.Result result = 2;</code>
     */
    public com.scalar.db.rpc.Result.Builder getResultBuilder(
        int index) {
      return getResultFieldBuilder().getBuilder(index);
    }
    /**
     * <code>repeated .rpc.Result result = 2;</code>
     */
    public com.scalar.db.rpc.ResultOrBuilder getResultOrBuilder(
        int index) {
      if (resultBuilder_ == null) {
        return result_.get(index);  } else {
        return resultBuilder_.getMessageOrBuilder(index);
      }
    }
    /**
     * <code>repeated .rpc.Result result = 2;</code>
     */
    public java.util.List<? extends com.scalar.db.rpc.ResultOrBuilder> 
         getResultOrBuilderList() {
      if (resultBuilder_ != null) {
        return resultBuilder_.getMessageOrBuilderList();
      } else {
        return java.util.Collections.unmodifiableList(result_);
      }
    }
    /**
     * <code>repeated .rpc.Result result = 2;</code>
     */
    public com.scalar.db.rpc.Result.Builder addResultBuilder() {
      return getResultFieldBuilder().addBuilder(
          com.scalar.db.rpc.Result.getDefaultInstance());
    }
    /**
     * <code>repeated .rpc.Result result = 2;</code>
     */
    public com.scalar.db.rpc.Result.Builder addResultBuilder(
        int index) {
      return getResultFieldBuilder().addBuilder(
          index, com.scalar.db.rpc.Result.getDefaultInstance());
    }
    /**
     * <code>repeated .rpc.Result result = 2;</code>
     */
    public java.util.List<com.scalar.db.rpc.Result.Builder> 
         getResultBuilderList() {
      return getResultFieldBuilder().getBuilderList();
    }
    private com.google.protobuf.RepeatedFieldBuilderV3<
        com.scalar.db.rpc.Result, com.scalar.db.rpc.Result.Builder, com.scalar.db.rpc.ResultOrBuilder> 
        getResultFieldBuilder() {
      if (resultBuilder_ == null) {
        resultBuilder_ = new com.google.protobuf.RepeatedFieldBuilderV3<
            com.scalar.db.rpc.Result, com.scalar.db.rpc.Result.Builder, com.scalar.db.rpc.ResultOrBuilder>(
                result_,
                ((bitField0_ & 0x00000001) != 0),
                getParentForChildren(),
                isClean());
        result_ = null;
      }
      return resultBuilder_;
    }

    private boolean hasMoreResults_ ;
    /**
     * <code>bool has_more_results = 3;</code>
     * @return The hasMoreResults.
     */
    @java.lang.Override
    public boolean getHasMoreResults() {
      return hasMoreResults_;
    }
    /**
     * <code>bool has_more_results = 3;</code>
     * @param value The hasMoreResults to set.
     * @return This builder for chaining.
     */
    public Builder setHasMoreResults(boolean value) {
      
      hasMoreResults_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>bool has_more_results = 3;</code>
     * @return This builder for chaining.
     */
    public Builder clearHasMoreResults() {
      
      hasMoreResults_ = false;
      onChanged();
      return this;
    }
    @java.lang.Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @java.lang.Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:rpc.ScanResponse)
  }

  // @@protoc_insertion_point(class_scope:rpc.ScanResponse)
  private static final com.scalar.db.rpc.ScanResponse DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.scalar.db.rpc.ScanResponse();
  }

  public static com.scalar.db.rpc.ScanResponse getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<ScanResponse>
      PARSER = new com.google.protobuf.AbstractParser<ScanResponse>() {
    @java.lang.Override
    public ScanResponse parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      Builder builder = newBuilder();
      try {
        builder.mergeFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(builder.buildPartial());
      } catch (com.google.protobuf.UninitializedMessageException e) {
        throw e.asInvalidProtocolBufferException().setUnfinishedMessage(builder.buildPartial());
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(e)
            .setUnfinishedMessage(builder.buildPartial());
      }
      return builder.buildPartial();
    }
  };

  public static com.google.protobuf.Parser<ScanResponse> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<ScanResponse> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.scalar.db.rpc.ScanResponse getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

