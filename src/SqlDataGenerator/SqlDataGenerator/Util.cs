using System;

namespace SqlDataGenerator {
    internal static class Util {
        public static DateTime RandomDateTime() {
            return RandomDateTime(new DateTime(1990, 1, 1), DateTime.Today);
        }

        public static DateTime RandomDateTime(DateTime minDate) {
            return RandomDateTime(minDate, DateTime.Today);
        }

        public static DateTime RandomDateTime(DateTime minDate, DateTime maxDate) {
            int range = Math.Abs((maxDate - minDate).Days - 1);   // We're going to add HH mm ss, so we subtract a day so that we don't exceed the range.
            return minDate.AddDays(rand.Next(range)).AddHours(rand.Next(0, 24)).AddMinutes(rand.Next(0, 60)).AddSeconds(rand.Next(0, 60));
        }

        public static DateTime Min(DateTime lhs, DateTime rhs) {
            return (lhs < rhs ? lhs : rhs);
        }

        public static DateTime Max(DateTime lhs, DateTime rhs) {
            return (lhs > rhs ? lhs : rhs);
        }

        public static string DateAsTimeStamp(DateTime date) {
            return DateAsTimeStamp((DateTime?)date);
        }

        public static string DateAsTimeStamp(DateTime? date) {
            if(date == null) {
                return "NULL";
            } else {
                return "TIMESTAMP '" + date.Value.ToString(DateFormat) + "'";
            }
        }

        public static Random rand = new Random(1);
        public const string DateFormat = "yyyy-MM-dd HH:mm:ss";
    }
}
