using System.Diagnostics;

namespace SqlDataGenerator {
    [DebuggerDisplay("group_id={group_id}, name={name}, desc={description}, capacity={capacity}")]
    public class Group {
        public Group(int id, int capacity) {
            this.group_id = id;
            this._name = RandomName();
            int useNullDesc = Util.rand.Next(3);
            if(useNullDesc == 0) {
                this._description = null;
            } else {
                this._description = "A group for " + this._name;
            }
            this.capacity = capacity;
        }

        public int group_id { get; }

        private string _name;
        public string name {
            get {
                if(_name == null) {
                    return "NULL";
                } else {
                    return $"'{_name}'";
                }
            }
        }

        private string _description;
        public string description {
            get {
                if(_description == null) {
                    return "NULL";
                } else {
                    return $"'{_description}'";
                }
            }
        }

        public int capacity { get; }

        private string RandomName() {
            return "Group" + Util.rand.Next(1000000);
        }
    }
}
