tinymce.PluginManager.add("imgupload", function(a) {
    a.addCommand("imgupload", function() {
        $("#imgupload").show();
    }), a.addButton("imgupload", {
        icon:"imgupload",
        tooltip:"上传图片",
        cmd:"imgupload"
    });
});