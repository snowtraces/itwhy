{
    let view = {
        el: '#main-wrapper',
        template: `
        <div id="home-search">
            <div class="search">
                <input type="search" name="keyword" autocomplete="off" placeholder="搜索...">
            </div>
        </div>
        <div id="home-list">
        </div>`,
        render(data) {
            $.el(this.el).innerHTML = $.evalTemplate(this.template, data)
        }
    }

    let model = {}

    let controller = {
        init(view, model) {
            this.view = view
            this.model = model
            this.bindEvents()
            this.bindEventHub()
        },
        bindEvents: function () {
            $.bindEvent('.search > input', 'keyup', (e) => {
                if (e.code === "Enter") {
                    this.onload($.el('.search > input').value)
                }
            })
        },
        bindEventHub() {
            window.eventHub.on(routes.home.event, () => {
                this.view.render(this.model.data)
                this.onload()
                this.bindEvents()
            })

            window.eventHub.on(routes.tag.event, ({value, loadView}) => {
                if (loadView) {
                    this.view.render(this.model.data)
                }
                this.onload(null, value)
            })
        },
        onload(query, tag) {
            $.get("https://my.snowtraces.com/qa/api/v1/sub/list", {
                query: query || "",
                tag: tag || ""
            }).then((res) => {
                let dataList = res.data || []
                $.el("#home-list").innerHTML = dataList.map(x => `
                    <div class="item">
                        <div class="item-title">
                            <a href="./#/sub/${x.subId}">${x.subTitle}</a>
                        </div>
                        <div class="item-tags ${!x.tags ? "hide" : ""}">${
                    !x.tags ? "" : x.tags.split(',').map(_t => `<a href="./#/tag/${_t}">${_t}</a>`).join("\n")
                }</div>
                    </div>
                `).join("\n")

                $.waterfall('#home-list', "#home-list > .item", 16)
            })
        },
    }

    controller.init(view, model)
}
