package org.kabiri.android.finance.model

data class PaymentEntry(
    var weekDay: WeekDays,
    var date: String,
    var description: String,
    var category: String,
    var value: String,
    var paymentType: String
) {
    enum class WeekDays {
        SAT, SUN, MON, TUE, WED, THU, FRI
    }
}