package com.scalar.db.schemaloader.command;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import com.google.common.collect.ImmutableMap;
import com.scalar.db.schemaloader.core.SchemaOperatorException;
import com.scalar.db.storage.dynamo.DynamoAdmin;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.Mockito;
import picocli.CommandLine;
import picocli.CommandLine.ExitCode;

public class DynamoCommandTest extends CommandTestBase {

  private static final String user = "aws_access_key";
  private static final String password = "aws_secret_key";
  private static final String ru = "10";
  private static final String region = "region";
  private static final Boolean noScaling = true;
  private static final Boolean noBackup = true;
  private static final String schemaFile = "path_to_file";

  @Override
  public void setUp() throws Exception {
    super.setUp();
    commandLine = new CommandLine(new DynamoCommand());
    setCommandLineOutput();
  }

  @Test
  public void
      call_WithProperCommandLineArgumentsForCreatingTables_ShouldCallCreateTableWithProperParams()
          throws SchemaOperatorException {
    // Arrange
    Map<String, String> metaOptions =
        ImmutableMap.<String, String>builder()
            .put(DynamoAdmin.REQUEST_UNIT, ru)
            .put(DynamoAdmin.NO_SCALING, noScaling.toString())
            .put(DynamoAdmin.NO_BACKUP, noBackup.toString())
            .build();

    // Act
    commandLine.execute(
        "-u",
        user,
        "-p",
        password,
        "--region",
        region,
        "--no-scaling",
        "--no-backup",
        "-r",
        ru,
        "-f",
        schemaFile);

    // Assert
    verify(operator).createTables(Mockito.any(), eq(metaOptions));
  }

  @Test
  public void call_WithProperCommandLineArgumentsForDeletingTables_ShouldCallDeleteTables()
      throws SchemaOperatorException {
    // Arrange

    // Act
    commandLine.execute("-u", user, "--region", region, "-p", password, "-f", schemaFile, "-D");

    // Assert
    verify(operator).deleteTables(Mockito.any());
  }

  @Test
  public void call_MissingSchemaFile_ShouldExitWithErrorCode() {
    // Arrange

    // Act
    int exitCode = commandLine.execute("-u", user, "-p", password, "--region", region);

    // Assert
    Assertions.assertThat(exitCode).isEqualTo(ExitCode.USAGE);
    Assertions.assertThat(stringWriter.toString())
        .contains("Missing required option '--schema-file=<schemaFile>'");
  }
}