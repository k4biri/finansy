package org.kabiri.android.finance

import android.app.DatePickerDialog
import android.content.Context
import java.text.SimpleDateFormat
import java.util.*

class DatePickerHelper {

    companion object {

        fun newInstance(context: Context, listener: OnDateStringSetListener): DatePickerDialog {

            val calendar = Calendar.getInstance()

            val datePickerDialogListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                listener.onDateSet(calendar)
            }

            val dlg = DatePickerDialog(
                context, datePickerDialogListener,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
            )

            return dlg
        }

        fun getWeekDayAndDate(calendar: Calendar): String {
            return SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(calendar.time)
        }
    }

    interface OnDateStringSetListener {
        fun onDateSet(selectedDate: Calendar)
    }
}