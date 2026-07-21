package com.accenture.document_sync_service.notification.template;

import org.springframework.stereotype.Component;

import com.accenture.document_sync_service.notification.model.ExecutionSummary;
import com.accenture.document_sync_service.notification.model.FailureSummary;


@Component
public class FailureEmailTemplateBuilder
        extends AbstractEmailTemplateBuilder {

    @Override
    public String build(ExecutionSummary summary) {

        StringBuilder html = new StringBuilder();

        appendHeader(html);

        appendFailureBanner(html);

        appendExecutionSummary(html, summary);

        appendFailureDetails(html, summary);

        appendSuggestedActions(html);

        appendFooter(html);

        return html.toString();
    }

    private void appendFailureBanner(StringBuilder html) {

    html.append("""
        <div class="section">

            <div style="
                background:#f8d7da;
                color:#721c24;
                border:1px solid #f5c6cb;
                padding:15px;
                border-radius:6px;
                text-align:center;
                font-size:18px;
                font-weight:bold;">

                ❌ EXECUTION FAILED

            </div>

        </div>
        """);
}

private void appendFailureDetails(
        StringBuilder html,
        ExecutionSummary summary) {

    FailureSummary failure = summary.getFailureSummary();

    html.append("""
        <div class="section">

            <h2>Failure Details</h2>

            <table>
        """);

    appendRow(html,
            "Failure Stage",
            failure.getStage().name());

    appendRow(html,
            "Exception",
            failure.getExceptionType());

    appendRow(html,
            "Error Message",
            failure.getErrorMessage());

    appendRow(html,
            "HTTP Status",
            String.valueOf(failure.getHttpStatus()));

    appendRow(html,
            "Retry Attempts",
            String.valueOf(failure.getRetryAttempts()));

    html.append("""
            </table>

        </div>
        """);
}


private void appendSuggestedActions(StringBuilder html) {

    html.append("""
        <div class="section">

            <h2>Suggested Actions</h2>

            <ul>
                <li>Verify DocuSign connectivity.</li>
                <li>Review application logs.</li>
                <li>Check Google Cloud Storage availability.</li>
                <li>Retry the scheduled execution if required.</li>
            </ul>

        </div>
        """);
}
}