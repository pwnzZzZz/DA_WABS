function scaleImageMap() {
    var image = document.getElementById('image');
    var map = document.querySelector('map[name="image-map"]');
    var areas = map.getElementsByTagName('area');

    var widthRatio = image.clientWidth / image.naturalWidth;
    var heightRatio = image.clientHeight / image.naturalHeight;

    for (var i = 0; i < areas.length; i++) {
        var coords = areas[i].getAttribute('coords').split(',');
        var scaledCoords = [];

        for (var j = 0; j < coords.length; j++) {
            if (j % 2 === 0) {
                // X coordinate
                scaledCoords.push(Math.round(coords[j] * widthRatio));
            } else {
                // Y coordinate
                scaledCoords.push(Math.round(coords[j] * heightRatio));
            }
        }

        areas[i].setAttribute('coords', scaledCoords.join(','));
    }


}