package org.kabiri.android.finance.model

import java.util.*

data class PaymentEntry(
    var weekDay: WeekDays,
    var date: Date,
    var description: String,
    var category: String,
    var value: Float,
    var paymentType: String
) {
    enum class WeekDays {
        SAT, SUN, MON, TUE, WED, THU, FRI
    }
}