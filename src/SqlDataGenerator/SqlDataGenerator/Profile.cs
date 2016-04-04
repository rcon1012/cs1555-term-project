using System;
using System.Collections.Generic;
using System.Diagnostics;

namespace SqlDataGenerator {
    [DebuggerDisplay("user_id={user_id}, fname={fname}, email={email}, dob={dob}, last_on{last_on}")]
    public class Profile {
        public Profile(int id) {
            this.user_id = id;
            this._fname = firstNames[Util.rand.Next(firstNames.Count)];
            this._lname = lastNames[Util.rand.Next(lastNames.Count)];
            this._email = _fname + _lname + emailDomains[Util.rand.Next(emailDomains.Count)];
            var date1 = Util.RandomDateTime();
            var date2 = Util.RandomDateTime();
            this.dob = Util.Min(date1, date2);
            this.last_on = Util.Max(date1, date2);
        }

        public int user_id { get; }

        private string _fname;
        public string fname {
            get {
                if(_fname == null) {
                    return "NULL";
                } else {
                    return $"'{_fname}'";
                }
            }
        }

        private string _lname;
        public string lname {
            get {
                if(_lname == null) {
                    return "NULL";
                } else {
                    return $"'{_lname}'";
                }
            }
        }

        private string _email;
        public string email {
            get {
                if(_email == null) {
                    return "NULL";
                } else {
                    return $"'{_email}'";
                }
            }
        }

        public DateTime dob { get; }

        public DateTime last_on { get; }

        public override int GetHashCode() {
            return user_id.GetHashCode();
        }

        public override bool Equals(object obj) {
            Profile other = obj as Profile;
            bool success = false;
            if(other != null) {
                success = this.user_id.Equals(other.user_id);
            }
            return success;
        }

        public static bool operator ==(Profile lhs, Profile rhs) {
            if(ReferenceEquals(lhs, null) || ReferenceEquals(rhs, null)) {
                return false;
            }

            return lhs.user_id == rhs.user_id;
        }

        public static bool operator !=(Profile lhs, Profile rhs) {
            return !(lhs == rhs);
        }

        private static IList<string> firstNames = new List<string>() {
            "Alice",
            "Bob",
            "Carol",
            "Dan",
            "Eve",
            "Frank",
            "Grace",
            "Mallory",
            "Oscar",
            "Peggy",
            "Trent",
            "Wendy",
            "Ryan",
            "Zach",
            "Jon",
            "Trevor",
            "Matt",
            "Steve",
            "Dana",
            "Sarah",
            "Joe",
            "Stephanie",
            "Alex",
            "Katie",
            "Chris",
            "Jamie",
            "Mary",
            "Nick",
            "Sam",
        };

        private static IList<string> lastNames = new List<string>() {
            "Smith",
            "Johnson",
            "Williams",
            "Jones",
            "Brown",
            "Davis",
            "Miller",
            "Wilson",
            "Moore",
            "Taylor",
            "Anderson",
            "Thomas",
            "Jackson",
            "White",
            "Harris",
            "Martin",
            "Thompson",
            "Garcia",
            "Martinez",
            "Robinson",
            "Lewis",
            "Lee",
            "Walker",
            "Hall",
            "Allen",
            "Young",
        };

        private static IList<string> emailDomains = new List<string>() {
            "@gmail.com",
            "@yahoo.com",
            "@outlook.com",
            "@aol.com",
            "@mail.com",
        };
    }
}
