const loadRoute = () => {
    let path = window.location.href
    let url_segs = path.split('/#/')
    if (url_segs.length >= 2) {
        let routeMeta = url_segs[1].split('/')
        window.eventHub.emit(routes[routeMeta[0]].event, routeMeta[1])
    } else {
        window.eventHub.emit(routes.home.event)
    }
}
loadRoute()
window.addEventListener("popstate", loadRoute, false);