// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: scalardb.proto

package com.scalar.db.rpc;

/**
 * Protobuf type {@code rpc.TransactionalScanRequest}
 */
public final class TransactionalScanRequest extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:rpc.TransactionalScanRequest)
    TransactionalScanRequestOrBuilder {
private static final long serialVersionUID = 0L;
  // Use TransactionalScanRequest.newBuilder() to construct.
  private TransactionalScanRequest(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private TransactionalScanRequest() {
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new TransactionalScanRequest();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private TransactionalScanRequest(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    if (extensionRegistry == null) {
      throw new java.lang.NullPointerException();
    }
    com.google.protobuf.UnknownFieldSet.Builder unknownFields =
        com.google.protobuf.UnknownFieldSet.newBuilder();
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          case 18: {
            com.scalar.db.rpc.Scan.Builder subBuilder = null;
            if (scan_ != null) {
              subBuilder = scan_.toBuilder();
            }
            scan_ = input.readMessage(com.scalar.db.rpc.Scan.parser(), extensionRegistry);
            if (subBuilder != null) {
              subBuilder.mergeFrom(scan_);
              scan_ = subBuilder.buildPartial();
            }

            break;
          }
          default: {
            if (!parseUnknownField(
                input, unknownFields, extensionRegistry, tag)) {
              done = true;
            }
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.scalar.db.rpc.ScalarDbProto.internal_static_rpc_TransactionalScanRequest_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.scalar.db.rpc.ScalarDbProto.internal_static_rpc_TransactionalScanRequest_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.scalar.db.rpc.TransactionalScanRequest.class, com.scalar.db.rpc.TransactionalScanRequest.Builder.class);
  }

  public static final int SCAN_FIELD_NUMBER = 2;
  private com.scalar.db.rpc.Scan scan_;
  /**
   * <code>.rpc.Scan scan = 2;</code>
   * @return Whether the scan field is set.
   */
  @java.lang.Override
  public boolean hasScan() {
    return scan_ != null;
  }
  /**
   * <code>.rpc.Scan scan = 2;</code>
   * @return The scan.
   */
  @java.lang.Override
  public com.scalar.db.rpc.Scan getScan() {
    return scan_ == null ? com.scalar.db.rpc.Scan.getDefaultInstance() : scan_;
  }
  /**
   * <code>.rpc.Scan scan = 2;</code>
   */
  @java.lang.Override
  public com.scalar.db.rpc.ScanOrBuilder getScanOrBuilder() {
    return getScan();
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
    if (scan_ != null) {
      output.writeMessage(2, getScan());
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (scan_ != null) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(2, getScan());
    }
    size += unknownFields.getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof com.scalar.db.rpc.TransactionalScanRequest)) {
      return super.equals(obj);
    }
    com.scalar.db.rpc.TransactionalScanRequest other = (com.scalar.db.rpc.TransactionalScanRequest) obj;

    if (hasScan() != other.hasScan()) return false;
    if (hasScan()) {
      if (!getScan()
          .equals(other.getScan())) return false;
    }
    if (!unknownFields.equals(other.unknownFields)) return false;
    return true;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    if (hasScan()) {
      hash = (37 * hash) + SCAN_FIELD_NUMBER;
      hash = (53 * hash) + getScan().hashCode();
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.scalar.db.rpc.TransactionalScanRequest parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.scalar.db.rpc.TransactionalScanRequest parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.scalar.db.rpc.TransactionalScanRequest parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.scalar.db.rpc.TransactionalScanRequest parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.scalar.db.rpc.TransactionalScanRequest parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.scalar.db.rpc.TransactionalScanRequest parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.scalar.db.rpc.TransactionalScanRequest parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.scalar.db.rpc.TransactionalScanRequest parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.scalar.db.rpc.TransactionalScanRequest parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.scalar.db.rpc.TransactionalScanRequest parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.scalar.db.rpc.TransactionalScanRequest parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.scalar.db.rpc.TransactionalScanRequest parseFrom(
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
  public static Builder newBuilder(com.scalar.db.rpc.TransactionalScanRequest prototype) {
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
   * Protobuf type {@code rpc.TransactionalScanRequest}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:rpc.TransactionalScanRequest)
      com.scalar.db.rpc.TransactionalScanRequestOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.scalar.db.rpc.ScalarDbProto.internal_static_rpc_TransactionalScanRequest_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.scalar.db.rpc.ScalarDbProto.internal_static_rpc_TransactionalScanRequest_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.scalar.db.rpc.TransactionalScanRequest.class, com.scalar.db.rpc.TransactionalScanRequest.Builder.class);
    }

    // Construct using com.scalar.db.rpc.TransactionalScanRequest.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
      }
    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      if (scanBuilder_ == null) {
        scan_ = null;
      } else {
        scan_ = null;
        scanBuilder_ = null;
      }
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.scalar.db.rpc.ScalarDbProto.internal_static_rpc_TransactionalScanRequest_descriptor;
    }

    @java.lang.Override
    public com.scalar.db.rpc.TransactionalScanRequest getDefaultInstanceForType() {
      return com.scalar.db.rpc.TransactionalScanRequest.getDefaultInstance();
    }

    @java.lang.Override
    public com.scalar.db.rpc.TransactionalScanRequest build() {
      com.scalar.db.rpc.TransactionalScanRequest result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.scalar.db.rpc.TransactionalScanRequest buildPartial() {
      com.scalar.db.rpc.TransactionalScanRequest result = new com.scalar.db.rpc.TransactionalScanRequest(this);
      if (scanBuilder_ == null) {
        result.scan_ = scan_;
      } else {
        result.scan_ = scanBuilder_.build();
      }
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
      if (other instanceof com.scalar.db.rpc.TransactionalScanRequest) {
        return mergeFrom((com.scalar.db.rpc.TransactionalScanRequest)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.scalar.db.rpc.TransactionalScanRequest other) {
      if (other == com.scalar.db.rpc.TransactionalScanRequest.getDefaultInstance()) return this;
      if (other.hasScan()) {
        mergeScan(other.getScan());
      }
      this.mergeUnknownFields(other.unknownFields);
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
      com.scalar.db.rpc.TransactionalScanRequest parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (com.scalar.db.rpc.TransactionalScanRequest) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private com.scalar.db.rpc.Scan scan_;
    private com.google.protobuf.SingleFieldBuilderV3<
        com.scalar.db.rpc.Scan, com.scalar.db.rpc.Scan.Builder, com.scalar.db.rpc.ScanOrBuilder> scanBuilder_;
    /**
     * <code>.rpc.Scan scan = 2;</code>
     * @return Whether the scan field is set.
     */
    public boolean hasScan() {
      return scanBuilder_ != null || scan_ != null;
    }
    /**
     * <code>.rpc.Scan scan = 2;</code>
     * @return The scan.
     */
    public com.scalar.db.rpc.Scan getScan() {
      if (scanBuilder_ == null) {
        return scan_ == null ? com.scalar.db.rpc.Scan.getDefaultInstance() : scan_;
      } else {
        return scanBuilder_.getMessage();
      }
    }
    /**
     * <code>.rpc.Scan scan = 2;</code>
     */
    public Builder setScan(com.scalar.db.rpc.Scan value) {
      if (scanBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        scan_ = value;
        onChanged();
      } else {
        scanBuilder_.setMessage(value);
      }

      return this;
    }
    /**
     * <code>.rpc.Scan scan = 2;</code>
     */
    public Builder setScan(
        com.scalar.db.rpc.Scan.Builder builderForValue) {
      if (scanBuilder_ == null) {
        scan_ = builderForValue.build();
        onChanged();
      } else {
        scanBuilder_.setMessage(builderForValue.build());
      }

      return this;
    }
    /**
     * <code>.rpc.Scan scan = 2;</code>
     */
    public Builder mergeScan(com.scalar.db.rpc.Scan value) {
      if (scanBuilder_ == null) {
        if (scan_ != null) {
          scan_ =
            com.scalar.db.rpc.Scan.newBuilder(scan_).mergeFrom(value).buildPartial();
        } else {
          scan_ = value;
        }
        onChanged();
      } else {
        scanBuilder_.mergeFrom(value);
      }

      return this;
    }
    /**
     * <code>.rpc.Scan scan = 2;</code>
     */
    public Builder clearScan() {
      if (scanBuilder_ == null) {
        scan_ = null;
        onChanged();
      } else {
        scan_ = null;
        scanBuilder_ = null;
      }

      return this;
    }
    /**
     * <code>.rpc.Scan scan = 2;</code>
     */
    public com.scalar.db.rpc.Scan.Builder getScanBuilder() {
      
      onChanged();
      return getScanFieldBuilder().getBuilder();
    }
    /**
     * <code>.rpc.Scan scan = 2;</code>
     */
    public com.scalar.db.rpc.ScanOrBuilder getScanOrBuilder() {
      if (scanBuilder_ != null) {
        return scanBuilder_.getMessageOrBuilder();
      } else {
        return scan_ == null ?
            com.scalar.db.rpc.Scan.getDefaultInstance() : scan_;
      }
    }
    /**
     * <code>.rpc.Scan scan = 2;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        com.scalar.db.rpc.Scan, com.scalar.db.rpc.Scan.Builder, com.scalar.db.rpc.ScanOrBuilder> 
        getScanFieldBuilder() {
      if (scanBuilder_ == null) {
        scanBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            com.scalar.db.rpc.Scan, com.scalar.db.rpc.Scan.Builder, com.scalar.db.rpc.ScanOrBuilder>(
                getScan(),
                getParentForChildren(),
                isClean());
        scan_ = null;
      }
      return scanBuilder_;
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


    // @@protoc_insertion_point(builder_scope:rpc.TransactionalScanRequest)
  }

  // @@protoc_insertion_point(class_scope:rpc.TransactionalScanRequest)
  private static final com.scalar.db.rpc.TransactionalScanRequest DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.scalar.db.rpc.TransactionalScanRequest();
  }

  public static com.scalar.db.rpc.TransactionalScanRequest getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<TransactionalScanRequest>
      PARSER = new com.google.protobuf.AbstractParser<TransactionalScanRequest>() {
    @java.lang.Override
    public TransactionalScanRequest parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new TransactionalScanRequest(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<TransactionalScanRequest> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<TransactionalScanRequest> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.scalar.db.rpc.TransactionalScanRequest getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}
