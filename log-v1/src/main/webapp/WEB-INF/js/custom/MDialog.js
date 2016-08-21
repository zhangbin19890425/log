/**
 * Created by wk on 2016/7/15.
 */
if (typeof MDialog !== 'object') {
    MDialog = {};
}

(function () {
    'use strict';
    MDialog.showDialog = function showDialog(url, width, height) {
        var iframe_height = height;
        var iframe_width = width;
        var d = dialog({
            title: 'message',
            width: width,
            height: height,
            content:'<iframe src="' + url + '" style="background:#fff"  height="' + iframe_height + '" width="' + iframe_width + '" frameborder="0" scrolling="no"></iframe>',
        });
        d.showModal();

    }

    function setScroll() {
        $("#chartsContainer").niceScroll({
            cursorcolor: "#CC0071",
            cursoropacitymax: 1,
            touchbehavior: false,
            cursorwidth: "5px",
            cursorborder: "0",
            cursorborderradius: "5px"
        });
    }

    MDialog.show = function show(url) {
        //700
    	MDialog.showDialog(url, 1020, 500);
    }

}());