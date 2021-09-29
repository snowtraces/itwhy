{
    let view = {
        el: '#main-wrapper',
        template: `
        <div id="main-sub">

        </div>
        <div id="main-ans-list">
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
        bindEvents() {

        },
        bindEventHub() {
            window.eventHub.on(routes.sub.event, (subId) => {
                this.view.render(this.model.data)
                this.onload(subId)
            })
        },
        onload(subId) {
            $.get("https://my.snowtraces.com/qa/api/v1/sub", {subId}).then((res) => {
                let sub = res.data || []
                $.el("#main-sub").innerHTML = `
                    <div class="sub-title"><h1>${sub.subTitle}</h1></div>
                    <div class="sub-desc">${sub.subDesc}</div>
                    <div class="author">${sub.addByName}</div>`

                if (sub.ansList.length) {
                    $.el("#main-ans-list").innerHTML = sub.ansList.map(ans => `
                        <div class="ans">
                            <div class="ans-desc">${ans.ansDesc}</div>
                            <div class="author">${ans.addByName}</div>
                        </div>
                       `).join("\n")
                }
                Prism.highlightAll()
            })

        }
    }

    controller.init(view, model)
}
