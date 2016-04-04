using System;

namespace SqlDataGenerator {
    public enum MsgRecipType {
        SingleUser = 1,
        WholeGroup = 2,

        Count
    }

    public class Message {
        public Message(int msg_id, Profile sender, Profile recip) {
            type = (int)MsgRecipType.SingleUser;
            recip_id = recip.user_id;
            Init(msg_id, sender);
        }

        public Message(int msg_id, Profile sender, Group recip) {
            type = (int)MsgRecipType.WholeGroup;
            recip_id = recip.group_id;
            Init(msg_id, sender);
        }

        private void Init(int msg_id, Profile sender) {
            this.msg_id = msg_id;
            this.subject = "subject " + this.msg_id;
            this.sender_id = sender.user_id;
            this.time_sent = Util.RandomDateTime(sender.dob, sender.last_on);
            if(Util.rand.Next(20) == 0) {
                this.msg_text = null;
            } else {
                this.msg_text = "message " + this.msg_id;
            }
        }
     
        public int msg_id { get; private set; }

        private string _subject;
        public string subject {
            get {
                if(_subject == null) {
                    return "NULL";
                } else {
                    return $"'{_subject}'";
                }
            }
            private set { _subject = value; }
        }

        public int sender_id { get; private set; }
        public int recip_id { get; private set; }
        public DateTime time_sent { get; private set; }

        private string _msg_text;
        public string msg_text {
            get {
                if(_msg_text == null) {
                    return "NULL";
                } else {
                    return $"'{_msg_text}'";
                }
            }
            private set { _msg_text = value; }
        }

        public int type { get; private set; }
    }
}
