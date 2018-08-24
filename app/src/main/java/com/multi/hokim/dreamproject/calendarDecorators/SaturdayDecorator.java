package com.multi.hokim.dreamproject.calendarDecorators;

import android.graphics.Color;
import android.text.style.ForegroundColorSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Calendar;

public class SaturdayDecorator implements DayViewDecorator {
    private final Calendar calendar = Calendar.getInstance();

    public SaturdayDecorator() {};

    @Override
    public boolean shouldDecorate(CalendarDay calendarDay) {
        calendarDay.copyTo(calendar);
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        return (weekDay == Calendar.SATURDAY);
    }

    @Override
    public void decorate(DayViewFacade dayViewFacade) {
        dayViewFacade.addSpan(new ForegroundColorSpan(Color.rgb(43, 53, 149)));
    }
}
