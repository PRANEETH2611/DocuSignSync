package com.accenture.document_sync_service.notification.template;

import org.springframework.stereotype.Component;

import com.accenture.document_sync_service.notification.model.ExecutionSummary;
import com.accenture.document_sync_service.notification.model.ProcessedEnvelopeSummary;

@Component
public class SuccessEmailTemplateBuilder
                extends AbstractEmailTemplateBuilder {

        @Override
        public String build(ExecutionSummary summary) {

                StringBuilder html = new StringBuilder();

                appendHeader(html);

                appendStatusBanner(html);

                appendExecutionSummary(html, summary);

                appendStatistics(html, summary);

                appendCheckpoint(html, summary);

                appendStorage(html, summary);

                appendProcessedDocuments(html, summary);

                appendFooter(html);

                return html.toString();
        }

        private void appendStatusBanner(StringBuilder html) {

                html.append("""
                                <div class="section">

                                    <div style="
                                        background:#d4edda;
                                        color:#155724;
                                        border:1px solid #c3e6cb;
                                        padding:15px;
                                        border-radius:6px;
                                        font-size:18px;
                                        font-weight:bold;
                                        text-align:center;">

                                        ✅ EXECUTION COMPLETED SUCCESSFULLY

                                    </div>

                                </div>
                                """);
        }

        private void appendStatistics(
                        StringBuilder html,
                        ExecutionSummary summary) {

                html.append("""
                                <div class="section">

                                    <h2>Processing Statistics</h2>

                                    <table>
                                """);

                appendRow(html,
                                "Envelopes Found",
                                String.valueOf(summary.getEnvelopesFound()));

                appendRow(html,
                                "Successfully Uploaded",
                                String.valueOf(summary.getUploaded()));

                appendRow(html,
                                "Skipped",
                                String.valueOf(summary.getSkipped()));

                appendRow(html,
                                "Failed",
                                String.valueOf(summary.getFailed()));

                html.append("""
                                    </table>

                                </div>
                                """);
        }

        private void appendCheckpoint(
                        StringBuilder html,
                        ExecutionSummary summary) {

                html.append("""
                                <div class="section">

                                    <h2>Checkpoint Information</h2>

                                    <table>
                                """);

                appendRow(
                                html,
                                "Previous Checkpoint",
                                String.valueOf(summary.getPreviousCheckpoint()));

                appendRow(
                                html,
                                "New Checkpoint",
                                String.valueOf(summary.getNewCheckpoint()));

                html.append("""
                                    </table>

                                </div>
                                """);
        }

        private void appendStorage(
                        StringBuilder html,
                        ExecutionSummary summary) {

                html.append("""
                                <div class="section">

                                    <h2>Google Cloud Storage</h2>

                                    <table>
                                """);

                appendRow(
                                html,
                                "Bucket Name",
                                summary.getBucketName());

                appendRow(
                                html,
                                "Folder",
                                summary.getBucketFolder());

                html.append("""
                                    </table>

                                </div>
                                """);
        }

        private void appendProcessedDocuments(
                        StringBuilder html,
                        ExecutionSummary summary) {

                html.append("""
                                <div class="section">

                                    <h2>Processed Documents</h2>

                                    <table>

                                        <tr>
                                            <th>Envelope ID</th>
                                            <th>Subject</th>
                                            <th>Document</th>
                                            <th>Completed At</th>
                                            <th>File Size</th>
                                            <th>Status</th>
                                        </tr>
                                """);

                if (summary.getProcessedEnvelopes() != null
                                && !summary.getProcessedEnvelopes().isEmpty()) {

                        for (ProcessedEnvelopeSummary envelope : summary.getProcessedEnvelopes()) {

                                html.append("<tr>");

                                appendCell(html, envelope.getEnvelopeId());

                                appendCell(html, envelope.getEnvelopeSubject());

                                appendCell(html, envelope.getDocumentName());

                                appendCell(html,
                                                String.valueOf(envelope.getCompletedAt()));

                                appendCell(html,
                                                String.valueOf(envelope.getFileSize()));

                                appendCell(html,
                                                envelope.getUploadStatus().name());

                                html.append("</tr>");
                        }

                } else {

                        html.append("""
                                        <tr>
                                            <td colspan="6"
                                                style="text-align:center;">
                                                No documents were processed.
                                            </td>
                                        </tr>
                                        """);
                }

                html.append("""
                                    </table>

                                </div>
                                """);
        }

}