var vm = new Vue({
    el: "#rapp",
    data: {
        yamlString: "",
        propertyString: ""
    },
    methods: {
        transformToProperty() {
            var that = this
            var url = "/transform/to/property";
            $.ajax({
                url: url,
                data: {
                    ymlString: that.yamlString
                },
                type: "post",
                dataType: "json",    //返回值的类型
                success: function (res) {
                    console.info(res)
                    if (res.code == 200) {
                        that.propertyString = res.data
                    } else {
                        that.$Message.error(res.message)
                    }
                },
                error:function(res){
                   var resdata = res.responseJSON
                    that.$Message.error(resdata.message)
                }
            })
        },

        transformToYML() {
            var that = this;
            var url = "/transform/to/yml"
            $.ajax({
                url: url,
                data: {
                    propertyString: that.propertyString
                },
                type: "post",
                dataType: "json",    //返回值的类型
                success: function (res) {
                    if (res.code == 200) {
                        that.yamlString = res.data
                    } else {
                        that.$Message.error(res.message)
                    }
                },
                error:function(res){
                    var resdata = res.responseJSON
                    that.$Message.error(resdata.message)
                }
            })
        }
    }
})