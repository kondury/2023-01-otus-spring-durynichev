package com.github.kondury.quiz.config;

public class MessageConfig {
    // acquaintance service
    public static final String WELCOME_MESSAGE_CODE = "acquaintance.welcome";
    public static final String INPUT_STUDENT_NAME_PROMPT_CODE = "acquaintance.input-name.prompt";
    // choice answer invitation
    public static final String ATTEMPT_MODE_CHOICE_ANSWER_PROMPT_CODE = "attempt.answer.choice.prompt";
    // text answer invitation
    public static final String ATTEMPT_MODE_TEXT_ANSWER_PROMPT_CODE = "attempt.answer.text.prompt";

    // Common input validation (blank or null source)
    public static final String ILLEGAL_ARGUMENT_VALUE_TMPL_CODE = "validation.blank-or-null";

    // student input validation
    public static final String TWO_PARTS_IN_NAME_TMPL_CODE = "acquaintance.input-name.validation.two-parts-required";
    public static final String EACH_PART_MUST_BE_MORE_THAN_TWO_SYMBOLS_CODE = "acquaintance.input-name.validation.two-symbols-parts";

    // choice answer validation
    public static final String INVALID_SOURCE_STRING_TMPL_CODE = "attempt.answer.choice.validation.invalid-input";
    public static final String INDEX_OUT_OF_RANGE_TMPL_CODE = "attempt.answer.choice.validation.index-out-of-range";

    // evaluation service
    public static final String QUIZ_PASSED_TMPL_CODE = "evaluation.result.passed";
    public static final String QUIZ_DIDNT_PASS_TMPL_CODE = "evaluation.result.didnt-pass";
}
