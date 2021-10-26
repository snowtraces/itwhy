{
    let view = {
        el: '#main-wrapper',
        template: `
        <div id="home-search">
            <div class="search">
                <input type="search" name="keyword" autocomplete="off" placeholder="搜索..." 
                value="\${data.tag ? \`[\${data.tag}] \` : ''}\${data.query ? \`\${data.query}\` : ''}">
            </div>
        </div>
        <div id="home-list">
        </div>`,
        render(data) {
            $.el(this.el).innerHTML = $.evalTemplate(this.template, data)
        }
    }

    let model = {
        filter: {}
    }

    let controller = {
        init(view, model) {
            this.view = view
            this.model = model
            this.bindEvents()
            this.bindEventHub()
        },
        bindEvents: function () {
            $.bindEvent('.search > input', 'change', (e) => {
                let search = $.el('.search > input').value
                window.location = search ? `./#/search/${search}` : './'
                if (search) {
                    let match = search.match(/(\[([^\]]+)\])? ?(.*)/)
                    this.model.filter.tag = match[2] || ''
                    this.model.filter.query = match[3] || ''
                    this.onload()
                }
            })
        },
        bindEventHub() {
            window.eventHub.on(routes.home.event, () => {
                this.model.filter = {}
                this.view.render(this.model.filter)
                this.onload()
                this.bindEvents()
            })

            window.eventHub.on(routes.search.event, ({value, loadView}) => {
                if (value) {
                    value = decodeURI(value)
                    let match = value.match(/(\[([^\]]+)\])? ?(.*)/)
                    this.model.filter.tag = match[2] || ''
                    this.model.filter.query = match[3] || ''

                    if (loadView) {
                        this.view.render(this.model.filter)
                    }
                    this.onload()
                }

            })
        },
        onload() {
            $.get("https://my.snowtraces.com/qa/api/v1/sub/list", this.model.filter).then((res) => {
                let dataList = res.data || []
                $.el("#home-list").innerHTML = dataList.map(x => `
                    <div class="item">
                        <div class="item-title">
                            <a href="./#/sub/${x.subId}">${x.subTitle}</a>
                        </div>
                        <div class="item-tags ${!x.tags ? "hide" : ""}">${
                    !x.tags ? "" : x.tags.split(',')
                        .map(_t => `<a href="./#/search/[${_t}]" class="${_t === this.model.filter.tag ? 'active' : ''}">${_t}</a>`).join("\n")
                }</div>
                    </div>
                `).join("\n")

                $.waterfall('#home-list', 16)
            })
        },
    }

    controller.init(view, model)
}
