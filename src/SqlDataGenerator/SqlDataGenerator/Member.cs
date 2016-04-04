using System.Diagnostics;

namespace SqlDataGenerator {
    [DebuggerDisplay("group_id={group_id}, user_id{user_id}")]
    class Member {
        public Member(int group_id, int user_id) {
            this.group_id = group_id;
            this.user_id = user_id;
        }

        public int group_id { get; }
        public int user_id { get; }

        public override int GetHashCode() {
            unchecked { // Overflow is fine, just wrap
                int hash = (int)2166136261;

                hash = (hash * 16777619) ^ group_id.GetHashCode();
                hash = (hash * 16777619) ^ user_id.GetHashCode();

                return hash;
            }
        }

        public override bool Equals(object obj) {
            Member other = obj as Member;
            bool success = false;
            if(other != null) {
                success = this.group_id.Equals(other.group_id) &&
                          this.user_id.Equals(other.user_id);
            }
            return success;
        }

        public static bool operator ==(Member lhs, Member rhs) {
            if(ReferenceEquals(lhs, null) || ReferenceEquals(rhs, null)) {
                return false;
            }

            return lhs.group_id == rhs.group_id &&
                   lhs.user_id == rhs.user_id;
        }

        public static bool operator !=(Member lhs, Member rhs) {
            return !(lhs == rhs);
        }
    }
}
