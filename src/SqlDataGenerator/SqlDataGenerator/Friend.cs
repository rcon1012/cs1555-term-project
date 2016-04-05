using System;
using System.Diagnostics;

namespace SqlDataGenerator {
    public enum FriendStatus {
        Pending = 0,
        Established = 1,

        Count,
    }

    [DebuggerDisplay("friend1_id={friend1_id}, friend2_id={friend2_id}, established={established}")]
    public class Friend {
        public Friend(Profile friend1, Profile friend2) {
            this.friend1_id = friend1.user_id;
            this.friend2_id = friend2.user_id;
            int status = Util.rand.Next((int)FriendStatus.Pending, (int)FriendStatus.Count);
            if(status == (int)FriendStatus.Pending) {
                this.established = null;
            } else {
                this.established = Util.RandomDateTime(Util.Max(friend1.dob, friend2.dob), Util.Max(friend1.last_on, friend2.last_on));    // Can occur after both parties are born to when either user was last online
            }
        }

        public int friend1_id { get; }
        public int friend2_id { get; }
        public DateTime? established { get; }

        public override int GetHashCode() {
            unchecked {
                int hash = (int)2166136261;

                hash = (hash * 16777619) ^ friend1_id.GetHashCode();
                hash = (hash * 16777619) ^ friend2_id.GetHashCode();

                return hash;
            }
        }

        public override bool Equals(object obj) {
            var other = obj as Friend;
            bool success = false;
            if(other != null) {
                success = this.friend1_id.Equals(other.friend1_id) &&
                          this.friend2_id.Equals(other.friend2_id);
            }
            return success;
        }

        public static bool operator ==(Friend lhs, Friend rhs) {
            if(ReferenceEquals(lhs, null) || ReferenceEquals(rhs, null)) {
                return false;
            }

            return lhs.friend1_id.Equals(rhs.friend1_id) &&
                   lhs.friend2_id.Equals(rhs.friend2_id);
        }

        public static bool operator !=(Friend lhs, Friend rhs) {
            return !(lhs == rhs);
        }
    }
}
