namespace SqlDataGenerator {
    class Member {
        public Member(int group_id, int user_id) {
            this.group_id = group_id;
            this.user_id = user_id;
        }

        public int group_id { get; }
        public int user_id { get; }
    }
}
