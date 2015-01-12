var gulp = require('gulp');
var browserify = require('gulp-browserify');

gulp.task('browserify', function() {
    gulp.src('src/main/resources/static/js/tailstreamer.js')
        .pipe(browserify({
            debug: true
        }))
        .pipe(gulp.dest('build/resources/main/static/js'))
});