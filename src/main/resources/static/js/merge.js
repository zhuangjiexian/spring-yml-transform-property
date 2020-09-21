var vm = new Vue({
    el: "#rapp",
    data: {
        ymlTop: '',
        ymlBottom: '',
        ymlMerge: ''
    },
    methods: {
        mergeYML() {
            var that = this
            var url = "/merge/yml";
            $.ajax({
                url: url,
                data: {
                    ymlTop: that.ymlTop,
                    ymlBottom: that.ymlBottom
                },
                type: "post",
                dataType: "json",    //返回值的类型
                success: function (res) {
                    if (res.code == 200) {
                        that.ymlMerge = res.data
                    } else {
                        that.$Message.error(res.message)
                    }
                },
                error: function (res) {
                    var resdata = res.responseJSON
                    that.$Message.error(resdata.message)
                }
            })
        }
    }
})